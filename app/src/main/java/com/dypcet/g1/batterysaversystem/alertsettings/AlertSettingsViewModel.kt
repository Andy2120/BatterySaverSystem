package com.dypcet.g1.batterysaversystem.alertsettings

import android.util.Log
import androidx.lifecycle.*
import com.dypcet.g1.batterysaversystem.R
import com.dypcet.g1.batterysaversystem.datasource.SharedPreferencesManager
import com.dypcet.g1.batterysaversystem.utils.ActionType
import com.dypcet.g1.batterysaversystem.utils.StateType

class AlertSettingsViewModel(
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    private val TAG = AlertSettingsViewModel::class.java.simpleName

    val percentage = MutableLiveData<Float>()

    private val currentPercentage = MutableLiveData<Float>()

    val checkCurrentPercentage = MutableLiveData<Boolean>()

    private val _alertState = MutableLiveData<StateType?>()
    val alertState: LiveData<StateType?> = _alertState

    private val _alertAction = MutableLiveData<ActionType?>()
    val alertAction: LiveData<ActionType?> = _alertAction

    private val _isChargingOn = MutableLiveData<Boolean>()
    val isChargingOn: LiveData<Boolean> = _isChargingOn

    val checkChargeState = MutableLiveData<Boolean>()

    val startButtonVisible = Transformations.map(_alertState) {
        it == StateType.STOPPED
    }

    val stopButtonVisible = Transformations.map(_alertState) {
        it == StateType.STARTED
    }

    init {

        onRefresh()

        percentage.value = sharedPreferencesManager.getAlertPercentage()

        _alertState.value = sharedPreferencesManager.getAlertState()
        Log.d(TAG, "init: alertState > ${_alertState.value}")
    }

    fun saveAlertStateAndPercentage() {
        sharedPreferencesManager.putAlertPercentage(percentage.value ?: 0F)
        sharedPreferencesManager.putAlertState(_alertState.value!!)
        Log.d(TAG, "saveAlertStateAndPercentage: saved ${_alertState.value} state")
    }

    fun setCurrentPercentage(value: Float) {
        currentPercentage.value = value
    }

    fun setChargingState(state: Boolean) {
        _isChargingOn.value = state
    }

    private val _snackbarText = MutableLiveData<Int?>()
    val snackbarText: LiveData<Int?> = _snackbarText

    fun onSetAlert() {
        // Check if device is in charging state
        onRefresh()
        if (_isChargingOn.value!!) {
            _snackbarText.postValue(R.string.is_charging)
            return
        }

        // Check if current battery level is less than
        // that of battery level set for alert
        checkCurrentPercentage.value = true
        val currentPercentage = currentPercentage.value ?: 0.0F
        if (percentage.value!! > currentPercentage) {
            _snackbarText.postValue(R.string.greater_than_maximum_battery)
            return
        }

        // Notify Alert is on
        _alertAction.value = ActionType.ON
    }

    fun doneSettingAlert() {
        _alertState.value = StateType.STARTED
    }

    fun onStopAlert() {
        // notify Alert is off
        _alertAction.value = ActionType.OFF
    }

    fun doneStoppingAlert() {
        _alertState.value = StateType.STOPPED
    }

    fun onRefresh() {
        checkChargeState.value = true
    }
}

@Suppress("UNCHECKED_CAST")
class AlertSettingsViewModelFactory(
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        (AlertSettingsViewModel(sharedPreferencesManager) as T)
}