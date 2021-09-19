package com.dypcet.g1.batterysaversystem.appdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.dypcet.g1.batterysaversystem.R

class AppDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_detail)

        findNavController(R.id.app_detail_host_fragment).setGraph(
            R.navigation.app_detail_nav,
            intent.extras
        )
    }
}