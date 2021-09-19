package com.dypcet.g1.batterysaversystem.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.dypcet.g1.batterysaversystem.R
import com.dypcet.g1.batterysaversystem.services.RingtoneService
import com.dypcet.g1.batterysaversystem.utils.sendNotification

class PowerConnectionReceiver : BroadcastReceiver() {

    private val TAG = PowerConnectionReceiver::class.java.simpleName

    private var isChargeCompleteNotified = false

    private var _percentage = 0.0F

    fun setPercentage(value: Float) {
        _percentage = value
    }

    override fun onReceive(context: Context, intent: Intent?) {

        val batteryPercentage: Float? = intent?.let { i ->
            val level: Int = i.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = i.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level * 100 / scale.toFloat()
        }
        Log.d(TAG, "Charge : $batteryPercentage %")

        if ((!isChargeCompleteNotified) && batteryPercentage!!.equals(_percentage)) {
            isChargeCompleteNotified = true
            val ringtone = RingtoneService.getInstance(context.applicationContext)
            ringtone.startAlarm()
            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager

            notificationManager.sendNotification(
                context.getString(R.string.charge_complete),
                context
            )
        }
    }
}