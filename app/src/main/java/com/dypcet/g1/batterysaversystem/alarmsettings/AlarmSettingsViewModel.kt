package com.dypcet.g1.batterysaversystem.alarmsettings

import androidx.lifecycle.*
import com.dypcet.g1.batterysaversystem.R
import com.dypcet.g1.batterysaversystem.datasource.SharedPreferencesManager
import com.dypcet.g1.batterysaversystem.utils.ActionType
import com.dypcet.g1.batterysaversystem.utils.StateType

class AlarmSettingsViewModel(
    private val sharedPreferenceManager: SharedPreferencesManager
) : ViewModel() {

    val percentage = MutableLiveData<Float>()

    private val currentPercentage = MutableLiveData<Float>()

    val checkCurrentPercentage = MutableLiveData<Boolean>()

    private var _alarmState = MutableLiveData<StateType?>()
    val alarmState: LiveData<StateType?> = _alarmState

    private val _alarmAction = MutableLiveData<ActionType?>()
    val alarmAction: LiveData<ActionType?> = _alarmAction

    private val _isChargingOn = MutableLiveData<Boolean>()
    val isChargingOn: LiveData<Boolean> = _isChargingOn

    val checkChargeState = MutableLiveData<Boolean>()

    val startButtonVisible = Transformations.map(_alarmState) {
        it == StateType.STOPPED
    }

    val stopButtonVisible = Transformations.map(_alarmState) {
        it == StateType.STARTED
    }

    init {

        onRefresh()

        percentage.value = sharedPreferenceManager.getAlarmPercentage()

        _alarmState.value = sharedPreferenceManager.getAlarmState()
    }

    fun saveAlarmStateAndPercentage() {
        sharedPreferenceManager.putAlarmPercentage(percentage.value ?: 0F)
        sharedPreferenceManager.putAlarmState(_alarmState.value!!)
    }

    fun setCurrentPercentage(value: Float) {
        currentPercentage.value = value
    }

    fun setChargingState(state: Boolean) {
        _isChargingOn.value = state
    }

    private val _snackbarText = MutableLiveData<Int?>()
    val snackbarText: LiveData<Int?> = _snackbarText

    fun onSetAlarm() {
        // Check if device is in charging state
        onRefresh()
        if (!_isChargingOn.value!!) {
            _snackbarText.postValue(R.string.not_charging)
            return
        }

        // Check if current battery level is greater than
        // that of battery level set for alarm
        checkCurrentPercentage.value = true
        val currentPercentage = currentPercentage.value ?: 0.0F
        if (percentage.value!! < currentPercentage) {
            _snackbarText.postValue(R.string.less_than_minimum_battery)
            return
        }

        // Notify Alarm is on
        _alarmAction.value = ActionType.ON
    }

    fun doneSettingAlarm() {
        _alarmState.value = StateType.STARTED
    }

    fun onStopAlarm() {
        // notify Alarm is off
        _alarmAction.value = ActionType.OFF
    }

    fun doneStoppingAlarm() {
        _alarmState.value = StateType.STOPPED
    }

    fun onRefresh() {
        checkChargeState.value = true
    }
}

@Suppress("UNCHECKED_CAST")
class AlarmSettingsViewModelFactory(
    private val sharedPreferenceManager: SharedPreferencesManager
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        (AlarmSettingsViewModel(sharedPreferenceManager) as T)
}