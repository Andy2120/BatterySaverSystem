package com.dypcet.g1.batterysaversystem.alertsettings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dypcet.g1.batterysaversystem.R
import com.dypcet.g1.batterysaversystem.datasource.FakeSharedPreferencesManager
import com.dypcet.g1.batterysaversystem.getOrAwaitValue
import com.dypcet.g1.batterysaversystem.utils.ActionType
import com.dypcet.g1.batterysaversystem.utils.StateType
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AlertSettingsViewModelTest {

    private lateinit var alertSettingsViewModel: AlertSettingsViewModel
    private lateinit var sharedPreferencesManager: FakeSharedPreferencesManager

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        sharedPreferencesManager = FakeSharedPreferencesManager()
        alertSettingsViewModel = AlertSettingsViewModel(sharedPreferencesManager)
        sharedPreferencesManager.initAlert(0.0F, StateType.STOPPED)
    }

    @Test
    fun onSetAlert_setAlertActionON() {
        // Given :
        alertSettingsViewModel.setChargingState(false)
        alertSettingsViewModel.percentage.value = 30.0F
        alertSettingsViewModel.setCurrentPercentage(80.0F)

        // When :
        alertSettingsViewModel.onSetAlert()

        val result = alertSettingsViewModel.alertAction.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(ActionType.ON))
    }

    @Test
    fun onStopAlert_setAlertActionOFF() {
        // When :
        alertSettingsViewModel.onStopAlert()

        val result = alertSettingsViewModel.alertAction.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(ActionType.OFF))
    }

    @Test
    fun setChargingState_setsChargingStateTrueOrFalse() {
        // When :
        alertSettingsViewModel.setChargingState(true)
        val result = alertSettingsViewModel.isChargingOn.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(true))
    }

    @Test
    fun onRefresh_setCheckChargeStateTrue() {
        // When :
        alertSettingsViewModel.onRefresh()
        val result = alertSettingsViewModel.checkChargeState.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(true))
    }

    @Test
    fun onSetAlertButChargingIsOn_setsSnackbarTextIsCharging() {
        // Given :
        alertSettingsViewModel.setChargingState(true)

        // When :
        alertSettingsViewModel.onSetAlert()

        val result = alertSettingsViewModel.snackbarText.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(R.string.is_charging))
    }

    @Test
    fun onSetAlertButPercentageIsGreaterThanCurrentPercentage_setsSnackbarTextGreaterThanBattery() {
        // Given :
        alertSettingsViewModel.setChargingState(false)
        alertSettingsViewModel.percentage.value = 80.0F
        alertSettingsViewModel.setCurrentPercentage(70.0F)

        // When :
        alertSettingsViewModel.onSetAlert()

        val result = alertSettingsViewModel.snackbarText.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(R.string.greater_than_maximum_battery))
    }

    @Test
    fun doneSettingAlert_setsAlertStateSTARTED() {
        // When :
        alertSettingsViewModel.doneSettingAlert()

        val result = alertSettingsViewModel.alertState.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(StateType.STARTED))
    }

    @Test
    fun doneStoppingAlert_setsAlertStateSTOPPED() {
        // When :
        alertSettingsViewModel.doneStoppingAlert()

        val result = alertSettingsViewModel.alertState.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(StateType.STOPPED))
    }

    @Test
    fun saveAlertStateAndPercentage_savesCurrentAlertStateAndPercentageSetForAlert() {
        // Given :
        alertSettingsViewModel.percentage.value = 30.0F
        alertSettingsViewModel.doneSettingAlert()

        // When :
        alertSettingsViewModel.saveAlertStateAndPercentage()
        val result1 = sharedPreferencesManager.getAlertState()
        val result2 = sharedPreferencesManager.getAlertPercentage()

        // Then :
        assertThat(result1, `is`(StateType.STARTED))
        assertThat(result2, `is`(30.0F))
    }

    @Test
    fun startButtonVisibleWhenAlertStateOFF_setsTrue() {
        // Given :
        alertSettingsViewModel.doneStoppingAlert()

        // When :
        val result = alertSettingsViewModel.startButtonVisible.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(true))
    }

    @Test
    fun stopButtonVisibleWhenAlertStateONOrSTARTED_setsTrue() {
        // Given :
        alertSettingsViewModel.doneSettingAlert()

        // When :
        val result = alertSettingsViewModel.stopButtonVisible.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(true))
    }
}