package com.dypcet.g1.batterysaversystem.datasource

import android.content.Context
import android.content.SharedPreferences
import com.dypcet.g1.batterysaversystem.R
import com.dypcet.g1.batterysaversystem.utils.StateType

class BatteryPreferencesManager(val applicationContext: Context) : SharedPreferencesManager {

    private val sharedPref: SharedPreferences = applicationContext.getSharedPreferences(
        applicationContext.getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )

    override fun putAlarmPercentage(percentage: Float) {
        with(sharedPref.edit()) {
            putFloat(
                applicationContext.getString(R.string.alarm_percentage_key),
                percentage
            )
            apply()
        }
    }

    override fun getAlarmPercentage(): Float {
        return sharedPref.getFloat(
            applicationContext.getString(R.string.alarm_percentage_key),
            0.0F
        )
    }

    override fun putAlarmState(state: StateType) {
        with(sharedPref.edit()) {
            putInt(
                applicationContext.getString(R.string.alarm_state_key),
                when (state) {
                    StateType.STARTED -> 1
                    else -> 0
                }
            )
            apply()
        }
    }

    override fun getAlarmState(): StateType {
        return when (sharedPref.getInt(
            applicationContext.getString(R.string.alarm_state_key),
            0
        )) {
            1 -> StateType.STARTED
            else -> StateType.STOPPED
        }
    }

    override fun putAlertPercentage(percentage: Float) {
        with(sharedPref.edit()) {
            putFloat(
                applicationContext.getString(R.string.alert_percentage_key),
                percentage
            )
            apply()
        }
    }

    override fun getAlertPercentage(): Float {
        return sharedPref.getFloat(
            applicationContext.getString(R.string.alert_percentage_key),
            0.0F
        )
    }

    override fun putAlertState(state: StateType) {
        with(sharedPref.edit()) {
            putInt(
                applicationContext.getString(R.string.alert_state_key),
                when (state) {
                    StateType.STARTED -> 1
                    else -> 0
                }
            )
            apply()
        }
    }

    override fun getAlertState(): StateType {
        return when (sharedPref.getInt(
            applicationContext.getString(R.string.alert_state_key),
            0
        )) {
            1 -> StateType.STARTED
            else -> StateType.STOPPED
        }
    }
}