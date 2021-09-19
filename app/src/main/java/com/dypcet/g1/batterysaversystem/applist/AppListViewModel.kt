package com.dypcet.g1.batterysaversystem.applist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dypcet.g1.batterysaversystem.datasource.DataSource
import com.dypcet.g1.batterysaversystem.models.InstalledApp

class AppListViewModel(
    private val dataSource: DataSource,
) : ViewModel() {

    var apps = MutableLiveData<List<InstalledApp>>()

    private val _navigateToAppDetail = MutableLiveData<String?>()
    val navigateToAppDetail
        get() = _navigateToAppDetail

    init {
        setFiltering(AppListFilterType.ALL_APPS)
    }

    fun setFiltering(requestType: AppListFilterType) {
        apps.value = dataSource.getApps(requestType)
    }

    fun onAppClicked(packageName: String) {
        _navigateToAppDetail.value = packageName
    }

    fun onAppDetailNavigated() {
        _navigateToAppDetail.value = null
    }
}

@Suppress("UNCHECKED_CAST")
class AppListViewModelFactory(
    private val dataSource: DataSource
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = (AppListViewModel(dataSource) as T)
}