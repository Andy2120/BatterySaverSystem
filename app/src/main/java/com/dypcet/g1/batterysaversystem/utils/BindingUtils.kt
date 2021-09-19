package com.dypcet.g1.batterysaversystem.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.dypcet.g1.batterysaversystem.R

@BindingAdapter("setAlarmBatteryPercentageText")
fun TextView.setTextForSettingBatteryPercentage(percentage: Float?) {
    text = this.context.getString(R.string.alarm_battery_percentage_string, percentage?.toInt())
}

@BindingAdapter("alarmSetDoneText")
fun TextView.setTextForAlarmSettingDone(percentage: Float?) {
    text = this.context.getString(R.string.alarm_set_done, percentage?.toInt())
}

@BindingAdapter("setAlertBatteryPercentageText")
fun TextView.setTextForSettingAlertBatteryPercentage(percentage: Float?) {
    text = this.context.getString(R.string.alert_battery_percentage_string, percentage?.toInt())
}

@BindingAdapter("alertSetDoneText")
fun TextView.setTextForAlertSettingDone(percentage: Float?) {
    text = this.context.getString(R.string.alert_set_done, percentage?.toInt())
}

