package com.dypcet.g1.batterysaversystem

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.dypcet.g1.batterysaversystem.datasource.BatteryPreferencesManager
import com.dypcet.g1.batterysaversystem.datasource.AppsDataSource
import com.dypcet.g1.batterysaversystem.datasource.SharedPreferencesManager
import com.dypcet.g1.batterysaversystem.utils.StateType

object ServiceLocator {

    @Volatile
    var sharedPreferencesManager: SharedPreferencesManager? = null
        @VisibleForTesting set

    @Volatile
    var dataSource: AppsDataSource? = null
        @VisibleForTesting set

    private val lock = Any()

    @VisibleForTesting
    fun resetPreferencesManager() {
        synchronized(lock) {
            sharedPreferencesManager?.apply {
                putAlarmPercentage(0.0F)
                putAlarmState(StateType.STOPPED)
                putAlertPercentage(0.0F)
                putAlertState(StateType.STOPPED)
            }
            sharedPreferencesManager = null
        }
    }

    fun provideSharedPreferencesManager(context: Context): SharedPreferencesManager {
        val newPreferencesManager = BatteryPreferencesManager(context.applicationContext)
        sharedPreferencesManager = newPreferencesManager

        return newPreferencesManager
    }

    fun provideDataSource(context: Context): AppsDataSource {
        val newDataSource = AppsDataSource(context.applicationContext)
        dataSource = newDataSource

        return newDataSource
    }
}