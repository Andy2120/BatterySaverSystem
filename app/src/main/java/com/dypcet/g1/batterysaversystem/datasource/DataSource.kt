package com.dypcet.g1.batterysaversystem.datasource

import com.dypcet.g1.batterysaversystem.applist.AppListFilterType
import com.dypcet.g1.batterysaversystem.models.InstalledApp

interface DataSource {

    fun getApps(flag: AppListFilterType): List<InstalledApp>

    fun getAppWithDetails(packageName: String): InstalledApp
}