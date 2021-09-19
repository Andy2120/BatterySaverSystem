package com.dypcet.g1.batterysaversystem.alertsettings

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dypcet.g1.batterysaversystem.BatterySaverApplication
import com.dypcet.g1.batterysaversystem.databinding.FragmentAlertSettingsBinding
import com.dypcet.g1.batterysaversystem.services.ChargeAlarmService
import com.dypcet.g1.batterysaversystem.services.RingtoneService
import com.dypcet.g1.batterysaversystem.utils.*
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar

class AlertSettingsFragment : Fragment() {

    private val TAG = AlertSettingsFragment::class.java.simpleName

    private lateinit var viewBinding: FragmentAlertSettingsBinding

    private val viewModel by viewModels<AlertSettingsViewModel> {
        AlertSettingsViewModelFactory(
            (requireContext().applicationContext as BatterySaverApplication).sharedPreferencesManager
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentAlertSettingsBinding.inflate(inflater, container, false)
            .apply {
                viewmodel = viewModel
            }
        viewBinding.lifecycleOwner = this.viewLifecycleOwner

        viewBinding.alertPercentageSlider.setLabelFormatter { value: Float -> "${value.toInt()}%" }
        viewBinding.alertPercentageSlider.addOnChangeListener(Slider.OnChangeListener { _, value, _ ->
            viewModel.percentage.value = value
        })

        viewModel.snackbarText.observe(viewLifecycleOwner, { string ->
            if (string != null) {
                Snackbar.make(requireView(), getString(string), Snackbar.LENGTH_SHORT).show()
            }
        })

        viewModel.alertAction.observe(viewLifecycleOwner, { action ->
            when (action) {
                ActionType.ON -> {
                    Intent(
                        requireContext(),
                        ChargeAlarmService::class.java
                    ).also { intent ->
                        intent.putExtra(PERCENTAGE_EXTRA, viewModel.percentage.value)
                        intent.putExtra(SERVICE_TYPE, SERVICE_ALERT)
                        requireContext().applicationContext.startService(intent)
                    }
                    viewModel.doneSettingAlert()
                    Log.d(TAG, "onCreateView: started Service")
                }
                ActionType.OFF -> {
                    RingtoneService.getInstance(requireContext().applicationContext)
                        .stopAlarmIfOn()

                    Intent(
                        requireContext(),
                        ChargeAlarmService::class.java
                    ).also { intent ->
                        requireContext().applicationContext.stopService(intent)
                    }
                    viewModel.doneStoppingAlert()
                    Log.d(TAG, "onCreateView: stopped Service")
                }
            }
        })

        viewModel.alertState.observe(viewLifecycleOwner, { state ->
            when (state) {
                StateType.STARTED -> {
                    viewBinding.moveSliderTextView.visibility = View.GONE
                    viewBinding.batteryPercentage.visibility = View.GONE
                    viewBinding.alertPercentageSlider.visibility = View.GONE
                    viewBinding.alertSetTextView.visibility = View.VISIBLE
                }
                StateType.STOPPED -> {
                    viewBinding.moveSliderTextView.visibility = View.VISIBLE
                    viewBinding.batteryPercentage.visibility = View.VISIBLE
                    viewBinding.alertPercentageSlider.visibility = View.VISIBLE
                    viewBinding.alertSetTextView.visibility = View.GONE
                }
            }
        })

        viewModel.checkChargeState.observe(viewLifecycleOwner, { shouldCheck ->
            if (shouldCheck) {
                viewModel.setChargingState(getChargeState())
                viewModel.checkChargeState.value = false
            }
        })

        viewModel.checkCurrentPercentage.observe(viewLifecycleOwner, { shouldCheck ->
            if (shouldCheck) {
                viewModel.setCurrentPercentage(getCurrentPercentage())
                viewModel.checkCurrentPercentage.value = false
            }
        })

        return viewBinding.root
    }


    private fun getChargeState(): Boolean {
        val batteryStatus: Intent = getBatteryChangedIntent()
        val status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1)

        return (status == BatteryManager.BATTERY_STATUS_CHARGING
                || status == BatteryManager.BATTERY_STATUS_FULL)
    }

    private fun getCurrentPercentage(): Float {
        val batteryStatus: Intent = getBatteryChangedIntent()

        return batteryStatus.let { intent ->
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level * 100 / scale.toFloat()
        }
    }

    private fun getBatteryChangedIntent(): Intent {
        return IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { intentFilter ->
            requireContext().registerReceiver(null, intentFilter)
        }!!
    }

    override fun onDestroy() {
        viewModel.saveAlertStateAndPercentage()
        super.onDestroy()
    }
}