package com.dypcet.g1.batterysaversystem.models

import android.graphics.drawable.Drawable

data class InstalledApp(
    var name: String? = "-",
    var packageName: String? = "-",
    var version: String? = "-1",
    var icon: Drawable? = null,
    var permissions: List<String>? = emptyList(),
    var services: List<String>? = emptyList()
)