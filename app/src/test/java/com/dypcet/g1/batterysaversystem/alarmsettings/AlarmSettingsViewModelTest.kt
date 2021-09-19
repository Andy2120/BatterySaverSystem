package com.dypcet.g1.batterysaversystem.alarmsettings

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


class AlarmSettingsViewModelTest {

    private lateinit var alarmSettingsViewModel: AlarmSettingsViewModel
    private lateinit var sharedPreferencesManager: FakeSharedPreferencesManager

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        sharedPreferencesManager = FakeSharedPreferencesManager()
        alarmSettingsViewModel = AlarmSettingsViewModel(sharedPreferencesManager)
        sharedPreferencesManager.initAlarm(0.0F, StateType.STOPPED)
    }

    @Test
    fun onSetAlarm_setsAlarmActionON() {
        // Given :
        alarmSettingsViewModel.setChargingState(true)
        alarmSettingsViewModel.percentage.value = 80.0F
        alarmSettingsViewModel.setCurrentPercentage(60.0F)

        // When :
        alarmSettingsViewModel.onSetAlarm()

        val result = alarmSettingsViewModel.alarmAction.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(ActionType.ON))
    }

    @Test
    fun onStopAlarm_setsAlarmActionOFF() {
        // When :
        alarmSettingsViewModel.onStopAlarm()

        val result = alarmSettingsViewModel.alarmAction.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(ActionType.OFF))
    }

    @Test
    fun setChargingState_setsChargingStateTrueOrFalse() {
        // When :
        alarmSettingsViewModel.setChargingState(true)
        val result = alarmSettingsViewModel.isChargingOn.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(true))
    }

    @Test
    fun onRefresh_setsCheckChargeStateTrue() {
        // When :
        alarmSettingsViewModel.onRefresh()
        val result = alarmSettingsViewModel.checkChargeState.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(true))
    }

    @Test
    fun onSetAlarmButChargingIsNotOn_setsSnackbarTextNotCharging() {
        // Given :
        alarmSettingsViewModel.setChargingState(false)

        // When :
        alarmSettingsViewModel.onSetAlarm()

        val result = alarmSettingsViewModel.snackbarText.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(R.string.not_charging))
    }

    @Test
    fun onSetAlarmButPercentageSetIsLessThanCurrentPercentage_setsSnackbarTextInsufficientBattery() {
        // Given :
        alarmSettingsViewModel.setChargingState(true)
        alarmSettingsViewModel.percentage.value = 50.0F
        alarmSettingsViewModel.setCurrentPercentage(60.0F)

        // When :
        alarmSettingsViewModel.onSetAlarm()

        val result = alarmSettingsViewModel.snackbarText.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(R.string.less_than_minimum_battery))
    }

    @Test
    fun doneSettingAlarm_setsAlarmStateSTARTED() {
        // When :
        alarmSettingsViewModel.doneSettingAlarm()

        val result = alarmSettingsViewModel.alarmState.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(StateType.STARTED))
    }

    @Test
    fun doneStoppingAlarm_setsAlarmActionSTOPPED() {
        // When :
        alarmSettingsViewModel.doneStoppingAlarm()

        val result = alarmSettingsViewModel.alarmState.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(StateType.STOPPED))
    }

    @Test
    fun saveAlarmStateAndPercentage_savesCurrentAlarmStateAndPercentageSetForAlarm() {
        // Given :
        alarmSettingsViewModel.percentage.value = 80.0F
        alarmSettingsViewModel.doneSettingAlarm()

        // When :
        alarmSettingsViewModel.saveAlarmStateAndPercentage()
        val result1 = sharedPreferencesManager.getAlarmState()
        val result2 = sharedPreferencesManager.getAlarmPercentage()

        // Then :
        assertThat(result1, `is`(StateType.STARTED))
        assertThat(result2, `is`(80.0F))
    }

    @Test
    fun startButtonVisibleWhenAlarmStateOFF_setsTrue() {
        // Given :
        alarmSettingsViewModel.doneStoppingAlarm()

        // When :
        val result = alarmSettingsViewModel.startButtonVisible.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(true))
    }

    @Test
    fun stopButtonVisibleWhenAlarmStateSTARTED_setsTrue() {
        // Given :
        alarmSettingsViewModel.doneSettingAlarm()

        // When :
        val result = alarmSettingsViewModel.stopButtonVisible.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(true))
    }
}