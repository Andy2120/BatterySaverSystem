package com.dypcet.g1.batterysaversystem.services

import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.util.Log

class RingtoneService(applicationContext: Context) {

    private val TAG = RingtoneService::class.java.simpleName

    private val ringtone = RingtoneManager.getRingtone(
        applicationContext,
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    )

    private var _isRingtoneOn = false

    companion object {

        @Volatile
        private var INSTANCE: RingtoneService? = null

        fun getInstance(applicationContext: Context): RingtoneService {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = RingtoneService(applicationContext)

                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    fun startAlarm() {
        Log.d(TAG, "Ringtone Started")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ringtone.isLooping = true
        }
        ringtone.play()
        _isRingtoneOn = true
    }

    fun stopAlarmIfOn() {
        if (_isRingtoneOn) {
            Log.d(TAG, "Ringtone Stopped")
            ringtone.stop()
            _isRingtoneOn = false
        }
    }
}