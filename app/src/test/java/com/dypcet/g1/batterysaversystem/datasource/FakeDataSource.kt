package com.dypcet.g1.batterysaversystem.datasource

import com.dypcet.g1.batterysaversystem.applist.AppListFilterType
import com.dypcet.g1.batterysaversystem.models.InstalledApp

class FakeDataSource(val listOfApps: List<InstalledApp>) : DataSource {

    override fun getApps(flag: AppListFilterType): List<InstalledApp> {
        return when (flag) {
            AppListFilterType.ALL_APPS -> listOfApps
            AppListFilterType.USER_APPS_ONLY -> listOfApps.subList(0, 1)
            AppListFilterType.SYSTEM_APPS_ONLY -> listOfApps.subList(1, 2)
            else -> listOfApps
        }
    }

    override fun getAppWithDetails(packageName: String): InstalledApp {
        var requiredApp = InstalledApp()
        listOfApps.forEach { app ->
            if (app.packageName.equals(packageName)) {
                requiredApp = app
                return@forEach
            }
        }

        return requiredApp
    }
}