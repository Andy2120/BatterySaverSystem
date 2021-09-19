package com.dypcet.g1.batterysaversystem.datasource

import com.dypcet.g1.batterysaversystem.utils.StateType

interface SharedPreferencesManager {
    fun putAlarmPercentage(percentage: Float)
    fun getAlarmPercentage(): Float

    fun putAlarmState(state: StateType)
    fun getAlarmState(): StateType

    fun putAlertState(state: StateType)
    fun getAlertState(): StateType

    fun putAlertPercentage(percentage: Float)
    fun getAlertPercentage(): Float
}