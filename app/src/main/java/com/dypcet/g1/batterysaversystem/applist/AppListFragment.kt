package com.dypcet.g1.batterysaversystem.applist

import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dypcet.g1.batterysaversystem.BatterySaverApplication
import com.dypcet.g1.batterysaversystem.R
import com.dypcet.g1.batterysaversystem.databinding.FragmentAppListBinding

class AppListFragment : Fragment() {

    private lateinit var viewBinding: FragmentAppListBinding
    private lateinit var adapter: AppListAdapter
    private lateinit var appOpsManager: AppOpsManager
    private val viewModel by viewModels<AppListViewModel> {
        AppListViewModelFactory(
            (requireContext().applicationContext as BatterySaverApplication).dataSource
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentAppListBinding.inflate(inflater, container, false)

        appOpsManager = requireActivity().getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager

        adapter = AppListAdapter(AppListener { app ->
            viewModel.onAppClicked(app.packageName!!)
        })

        viewBinding.appListView.adapter = adapter

        viewModel.apps.observe(viewLifecycleOwner, { apps ->
            adapter.submitList(apps)
        })

        viewModel.navigateToAppDetail.observe(viewLifecycleOwner, { packageName ->
            packageName?.let {
                this.findNavController().navigate(
                    AppListFragmentDirections.actionAppListFragmentToAppDetailActivity(packageName)
                )
                viewModel.onAppDetailNavigated()
            }
        })

        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
    }

    override fun onPause() {
        super.onPause()
        setHasOptionsMenu(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.filter_all -> {
            Toast.makeText(context, "Filter All", Toast.LENGTH_SHORT).show()
            viewModel.setFiltering(AppListFilterType.ALL_APPS)
            true
        }
        R.id.filter_userapps_only -> {
            Toast.makeText(context, "Filter User Apps", Toast.LENGTH_SHORT).show()
            viewModel.setFiltering(AppListFilterType.USER_APPS_ONLY)
            true
        }
        R.id.filter_system_only -> {
            Toast.makeText(context, "Filter System Apps", Toast.LENGTH_SHORT).show()
            viewModel.setFiltering(AppListFilterType.SYSTEM_APPS_ONLY)
            true
        }
        R.id.filter_active -> {
            val mode = appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                requireActivity().packageName
            )
            when (mode) {
                AppOpsManager.MODE_ALLOWED -> {
                    Toast.makeText(context, "Active Apps", Toast.LENGTH_LONG).show()
                    viewModel.setFiltering(AppListFilterType.ACTIVE_APPS_ONLY)
                }
                else -> {
                    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                    startActivityForResult(intent, 0)
                }
            }
            true
        }
        else -> false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
    }
}