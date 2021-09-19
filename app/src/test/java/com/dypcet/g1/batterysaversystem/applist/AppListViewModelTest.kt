package com.dypcet.g1.batterysaversystem.applist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dypcet.g1.batterysaversystem.datasource.FakeDataSource
import com.dypcet.g1.batterysaversystem.getOrAwaitValue
import com.dypcet.g1.batterysaversystem.models.InstalledApp
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.nullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AppListViewModelTest {

    private lateinit var appListViewModel: AppListViewModel
    private lateinit var dataSource: FakeDataSource

    private lateinit var appsList: List<InstalledApp>

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        appsList = listOf(
            InstalledApp(
                "Application 1",
                "com.test.app1",
                "1.2.3",
                permissions = listOf("Permission 1", "Permission 2"),
                services = listOf("Services 1", "Services 2")
            ),
            InstalledApp(
                "Application 2",
                "com.test.app2",
                "3.1.2",
                permissions = listOf("Permission 1", "Permission 2"),
                services = listOf("Services 1", "Services 2")
            ),
            InstalledApp(
                "Application 3",
                "com.test.app3",
                "2.3.1",
                permissions = listOf("Permission 1", "Permission 2"),
                services = listOf("Services 1", "Services 2")
            )
        )
        dataSource = FakeDataSource(appsList)
        appListViewModel = AppListViewModel(dataSource)
    }

    @Test
    fun onInitGetApps_returnsListOfApps() {
        // When :
        val result = appListViewModel.apps.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(appsList))
    }

    @Test
    fun setFilteringUserAppsOnly_returnsOnlyUserInstalledApps() {
        // When :
        appListViewModel.setFiltering(AppListFilterType.USER_APPS_ONLY)

        val result = appListViewModel.apps.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(listOf(appsList[0])))
    }

    @Test
    fun setFilteringSystemAppsOnly_returnsOnlySystemInstalledApps() {
        // When :
        appListViewModel.setFiltering(AppListFilterType.SYSTEM_APPS_ONLY)

        val result = appListViewModel.apps.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(listOf(appsList[1])))
    }

    @Test
    fun onAppClicked_setsNavigateToAppDetailPackageName() {
        // When :
        appListViewModel.onAppClicked("some.package.app")

        val result = appListViewModel.navigateToAppDetail.getOrAwaitValue()

        // Then :
        assertThat(result, `is`("some.package.app"))
    }

    @Test
    fun onAppDetailNavigated_setsNavigateToAppDetailNull() {
        // Given :
        appListViewModel.onAppClicked("some.package.app")

        // When :
        appListViewModel.onAppDetailNavigated()
        val result = appListViewModel.navigateToAppDetail.getOrAwaitValue()

        // Then :
        assertThat(result, `is`(nullValue()))
    }
}