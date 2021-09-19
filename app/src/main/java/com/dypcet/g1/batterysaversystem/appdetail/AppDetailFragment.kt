package com.dypcet.g1.batterysaversystem.appdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.dypcet.g1.batterysaversystem.BatterySaverApplication
import com.dypcet.g1.batterysaversystem.R
import com.dypcet.g1.batterysaversystem.databinding.FragmentAppDetailBinding

class AppDetailFragment : Fragment() {

    private lateinit var viewBinding: FragmentAppDetailBinding

    private val args: AppDetailFragmentArgs by navArgs()

    private val detailViewModel by viewModels<AppDetailViewModel> {
        AppDetailViewModelFactory(
            (requireContext().applicationContext as BatterySaverApplication).dataSource,
            args.packageName
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewBinding = FragmentAppDetailBinding.inflate(inflater, container, false)
            .apply {
                viewModel = detailViewModel
            }
        viewBinding.lifecycleOwner = this.viewLifecycleOwner

        viewBinding.appIconView.setImageDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.ic_launcher_background
            )
        )

        detailViewModel.app.observe(viewLifecycleOwner, { app ->
            if (app != null) {
                viewBinding.appIconView.setImageDrawable(
                    app.icon ?: AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_launcher_background
                    )
                )
                val detailList = HashMap<String, List<String>>()

                detailList["Permissions"] = app.permissions ?: emptyList()
                detailList["Services"] = app.services ?: emptyList()

                val titleList = arrayListOf("Permissions", "Services")

                val expandableListAdapter = MyExpandableListAdapter(titleList, detailList)
                viewBinding.expandableList.setAdapter(expandableListAdapter)
            }
        })

        detailViewModel.navigateToApp.observe(viewLifecycleOwner, {
            if (it) {
                val launchIntent = requireActivity()
                    .packageManager
                    .getLaunchIntentForPackage(args.packageName)
                startActivity(launchIntent)
                detailViewModel.doneOpeningApp()
            }
        })

        detailViewModel.navigateToAppSettings.observe(viewLifecycleOwner, {
            if (it) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", args.packageName, null)
                startActivity(intent)
                detailViewModel.doneClosingApp()
            }
        })

        return viewBinding.root
    }
}