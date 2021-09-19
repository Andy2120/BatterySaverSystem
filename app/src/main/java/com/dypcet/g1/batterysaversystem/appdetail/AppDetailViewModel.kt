package com.dypcet.g1.batterysaversystem.appdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dypcet.g1.batterysaversystem.datasource.DataSource
import com.dypcet.g1.batterysaversystem.models.InstalledApp

class AppDetailViewModel(
    dataSource: DataSource,
    packageName: String
) : ViewModel() {

    private val _app = MutableLiveData<InstalledApp>()
    val app: LiveData<InstalledApp> = _app

    init {
        _app.value = dataSource.getAppWithDetails(packageName)
    }

    private val _navigateToApp = MutableLiveData<Boolean>()
    val navigateToApp = _navigateToApp

    private val _navigateToAppSettings = MutableLiveData<Boolean>()
    val navigateToAppSettings = _navigateToAppSettings

    fun openApp() {
        _navigateToApp.value = true
    }

    fun doneOpeningApp() {
        _navigateToApp.value = false
    }

    fun closeApp() {
        _navigateToAppSettings.value = true
    }

    fun doneClosingApp() {
        _navigateToAppSettings.value = false
    }
}

@Suppress("UNCHECKED_CAST")
class AppDetailViewModelFactory(
    private val dataSource: DataSource,
    private val packageName: String
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        (AppDetailViewModel(dataSource, packageName) as T)
}