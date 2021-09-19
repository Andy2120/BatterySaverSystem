package com.dypcet.g1.batterysaversystem.appdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dypcet.g1.batterysaversystem.datasource.FakeDataSource
import com.dypcet.g1.batterysaversystem.getOrAwaitValue
import com.dypcet.g1.batterysaversystem.models.InstalledApp
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AppDetailViewModelTest {

    private lateinit var appDetailViewModel: AppDetailViewModel
    private lateinit var dataSource: FakeDataSource

    private lateinit var app1: InstalledApp

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        app1 = InstalledApp(
            "Application 1",
            "com.my.test.app1",
            "1.2.3",
            permissions = listOf("Permission 1", "Permission 2"),
            services = listOf("Service 1", "Service 2")
        )

        val app2 = InstalledApp(
            "Application 2",
            "com.my.test.app2",
            "1.3.2",
            permissions = listOf("Permission 1", "Permission 2"),
            services = listOf("Service 1", "Service 2")
        )

        dataSource = FakeDataSource(listOf(app1, app2))

        appDetailViewModel = AppDetailViewModel(dataSource, "com.my.test.app1")
    }

    @Test
    fun onInitGetApplication_returnsRequiredApplication() {

        val result = appDetailViewModel.app.getOrAwaitValue()

        assertThat(result, `is`(app1))
    }

    @Test
    fun openApp_setsNavigateToAppTrue() {

        appDetailViewModel.openApp()
        val result = appDetailViewModel.navigateToApp.getOrAwaitValue()

        assertThat(result, `is`(true))
    }

    @Test
    fun closeApp_setsNavigateToAppSettingsTrue() {

        appDetailViewModel.closeApp()
        val result = appDetailViewModel.navigateToAppSettings.getOrAwaitValue()

        assertThat(result, `is`(true))
    }

    @Test
    fun doneOpeningApp_setsNavigateToAppFalse() {

        appDetailViewModel.openApp()

        appDetailViewModel.doneOpeningApp()
        val result = appDetailViewModel.navigateToApp.getOrAwaitValue()

        assertThat(result, `is`(false))
    }

    @Test
    fun doneClosingApp_setsNavigateToAppSettingsFalse() {

        appDetailViewModel.closeApp()

        appDetailViewModel.doneClosingApp()
        val result = appDetailViewModel.navigateToAppSettings.getOrAwaitValue()

        assertThat(result, `is`(false))
    }
}