package com.example.foser

import android.app.*
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi


class MyForegroundService : Service() {

    companion object {
        val CHANNEL_ID = "MyForegroundServiceChannel"
        val CHANNEL_NAME = "FoSer service channel"

        val MESSAGE = "message"
        val TIME = "time"
        val WORK = "work"
        val WORK_DOUBLE = "work_double"

    }

    private var message: String? = null
    private var showTime: Boolean? = null
    private var doWork: Boolean? = null
    private var doubleSpeed: Boolean? = null

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun doWork() {
        Thread.sleep(5000)
        val info = """Start working...
                    show_time=${showTime.toString()}
                    do_work=${doWork.toString()}
                    double_speed=${doubleSpeed.toString()}"""

        Toast.makeText(this, info, Toast.LENGTH_LONG).show()
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //return super.onStartCommand(intent, flags, startId)

        message = intent?.getStringExtra(MESSAGE)
        showTime = intent?.getBooleanExtra(TIME, false)
        doWork = intent?.getBooleanExtra(WORK, false)
        doubleSpeed = intent?.getBooleanExtra(WORK_DOUBLE, false)

        createNotificationChannel()

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)


        val notification: Notification = Notification.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_my_icon)
            .setContentTitle(getString(R.string.ser_title))
            .setShowWhen(showTime!!)
            .setContentText(message)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.circle))
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)


        doWork()

        return START_NOT_STICKY
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }
}
