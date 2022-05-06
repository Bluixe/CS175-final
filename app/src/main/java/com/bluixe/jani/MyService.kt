package com.bluixe.jani

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.room.Room
import java.util.Calendar

class MyService : Service() {
    lateinit var db: TodoItemDatabase
    lateinit var dao: TodoItemDao
    lateinit var timeChangeReceiver: TimeChangeReceiver
    lateinit var manager: NotificationManager
    lateinit var intent: Intent
    lateinit var pi: PendingIntent
    lateinit var notification: Notification
    override fun onCreate() {
        super.onCreate()
        Log.d("bluixelog", "hello")
        db = Room.databaseBuilder(
            applicationContext,
            TodoItemDatabase::class.java, "todoitem"
        ).build()
        dao = db.todoItemDao()
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.TIME_TICK")
        timeChangeReceiver = TimeChangeReceiver()
        registerReceiver(timeChangeReceiver, intentFilter)
        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("normal", "DDL", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        intent = Intent(this, MainActivity::class.java)
        pi = PendingIntent.getActivity(this, 0, intent, 0)
        notification = NotificationCompat.Builder(this, "normal")
            .setContentTitle("DDL")
            .setContentText("1小时内有DDL！")
            .setContentIntent(pi)
            .setSmallIcon(R.drawable.ic_magnify)
            .build()
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("bluixelog", "hello2")
        return super.onStartCommand(intent, flags, startId)

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("bluixelog", "hello3")
        unregisterReceiver(timeChangeReceiver)
    }

    inner class TimeChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("bluixelog", "hey")
//            manager.notify(1,notification)
            var calendar = Calendar.getInstance()
            var time = ((calendar.get(Calendar.YEAR)*100 + calendar.get(Calendar.MONTH)+1)*100.toLong() + calendar.get(
                Calendar.DAY_OF_MONTH))*10000 + (calendar.get(Calendar.HOUR_OF_DAY)+1)*100 + calendar.get(Calendar.MINUTE)
            Log.d("bluixelog", time.toString())
            Thread {
                var data = dao.queryTime(time)
                if (data.isNotEmpty()) {
                    manager.notify(1,notification)
                }
            }.start()
        }

    }
}