package com.dypcet.g1.batterysaversystem

import android.app.Application
import com.dypcet.g1.batterysaversystem.datasource.AppsDataSource
import com.dypcet.g1.batterysaversystem.datasource.SharedPreferencesManager

class BatterySaverApplication : Application() {

    val sharedPreferencesManager: SharedPreferencesManager
        get() = ServiceLocator.provideSharedPreferencesManager(this)

    val dataSource: AppsDataSource
        get() = ServiceLocator.provideDataSource(this)
}