package com.dypcet.g1.batterysaversystem.datasource

import com.dypcet.g1.batterysaversystem.utils.StateType

class FakeSharedPreferencesManagerAndroid : SharedPreferencesManager {

    private var percentage: Float? = null
    private var alertPercentage: Float? = null
    private var alarmState: StateType? = null
    private var alertState: StateType? = null

    fun initAlarm(percentage: Float, state: StateType) {
        this.percentage = percentage
        alarmState = state
    }

    fun initAlert(percentage: Float, state: StateType) {
        this.alertPercentage = percentage
        alertState = state
    }

    override fun putAlarmPercentage(percentage: Float) {
        this.percentage = percentage
    }

    override fun getAlarmPercentage(): Float {
        return percentage ?: 0.0F
    }

    override fun putAlarmState(state: StateType) {
        alarmState = state
    }

    override fun getAlarmState(): StateType {
        return alarmState ?: StateType.STOPPED
    }

    override fun putAlertState(state: StateType) {
        alertState = state
    }

    override fun getAlertState(): StateType {
        return alertState ?: StateType.STOPPED
    }

    override fun putAlertPercentage(percentage: Float) {
        alertPercentage = percentage
    }

    override fun getAlertPercentage(): Float {
        return alertPercentage ?: 0.0F
    }
}