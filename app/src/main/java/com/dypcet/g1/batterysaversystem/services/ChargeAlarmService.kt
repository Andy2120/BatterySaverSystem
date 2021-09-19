package com.dypcet.g1.batterysaversystem.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import com.dypcet.g1.batterysaversystem.MainActivity
import com.dypcet.g1.batterysaversystem.R
import com.dypcet.g1.batterysaversystem.receivers.PowerConnectionReceiver
import com.dypcet.g1.batterysaversystem.utils.*

class ChargeAlarmService : Service() {

    private val TAG = ChargeAlarmService::class.java.simpleName

    private val receiver = PowerConnectionReceiver()

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            receiver.setPercentage(msg.arg2.toFloat())

            IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { intentFilter ->
                applicationContext.registerReceiver(
                    receiver,
                    intentFilter
                )
                Log.d(TAG, "onStartCommand receiver registered")
            }
        }
    }

    override fun onCreate() {
        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()

            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand Called")
        val percentage = intent?.getFloatExtra(PERCENTAGE_EXTRA, 0.0F)
        val serviceType = intent?.getStringExtra(SERVICE_TYPE)
        Log.d(TAG, "onStartCommand Percentage : $percentage")

        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            if (percentage != null) {
                msg.arg2 = percentage.toInt()
            }
            serviceHandler?.sendMessage(msg)
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, notificationIntent, FLAGS)

        val notification = NotificationCompat.Builder(
            this,
            applicationContext.getString(R.string.notification_channel_id)
        )
            .setContentTitle(
                when (serviceType) {
                    SERVICE_ALARM -> "Charge Alarm Service"
                    SERVICE_ALERT -> "Charge Alert Service"
                    else -> "Unintended Service call"
                }
            )
            .setContentText(
                when (serviceType) {
                    SERVICE_ALARM -> "Charge Alarm set to $percentage"
                    SERVICE_ALERT -> "Charge Alert set to $percentage"
                    else -> "Unintended Service call"
                }
            )
            .setSmallIcon(
                when (serviceType) {
                    SERVICE_ALARM -> R.drawable.alarm_charge
                    SERVICE_ALERT -> R.drawable.alert_charge
                    else -> R.drawable.ic_launcher_foreground
                }
            )
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy Called")
        applicationContext.unregisterReceiver(receiver)
        Log.d(TAG, "onDestroy receiver unregistered")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}