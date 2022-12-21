package com.hexflake.rajutaskevince.service

import android.R.id.icon1
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.hexflake.rajutaskevince.R
import com.hexflake.rajutaskevince.ui.screens.MainActivity
import com.hexflake.rajutaskevince.utils.downloadImage
import com.hexflake.rajutaskevince.utils.exceptionCoroutine
import com.hexflake.rajutaskevince.utils.saveimage
import kotlinx.coroutines.*


class ForegroundService : Service() {
    override fun onCreate() {
        super.onCreate()
    }
    /**
     * job and scope initlization for perform operation in backgroudn
     * */
    val job = Job()
    val serviceScop = CoroutineScope(job + Dispatchers.IO)

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val input = intent.getStringExtra("url")
        createNotificationChannel()
        //set delay for 3 sec so can see on going notification
        val notification = buildNotificaitonObject("Image Downloading(delay with 3 sec)", null)
        startForeground(1, notification)

        //doing main process first download then save to provate storage
        serviceScop.launch(Dispatchers.IO + exceptionCoroutine) {

            //puted manully delay so user can see iamge is donwloaaidng (in fast internet foreground service instanly show then hide)
            delay(3000)

            downloadImage(input) { bitmap: Bitmap? ->
                bitmap?.let {
                    saveimage(filesDir, bitmap) { file ->

                        //show downloaded image to notification
                        showSuccessNotificationWithDownloadedImage(bitmap)

                        // stop service after image downloadedd and
                        stopSelf()
                    }
                } ?: kotlin.run {
                    showSuccessNotificationWithDownloadedImage(null)

                    // stop service on Failed
                    stopSelf()
                }

            }
        }
        return START_NOT_STICKY
    }

    private fun showSuccessNotificationWithDownloadedImage(bitmap: Bitmap?) {
        val messsage = if (bitmap == null) {
            "Download Failed,Check Internet"
        } else {
            "Saved in android private storage"
        }
        val notification = buildNotificaitonObject(messsage, bitmap)
        val mNotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(0, notification)
    }


    /**
     *
     * provide notificaiton object with given title and bitmap
     * */
    private fun buildNotificaitonObject(title: String, image: Bitmap?): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText("")
            .setSmallIcon(R.drawable.ic_down)
            .setContentIntent(pendingIntent).apply {
                image?.let {
                    val bigPicture = NotificationCompat.BigPictureStyle()
                    bigPicture.bigPicture(it)
                    setLargeIcon(it)
                        .setStyle(bigPicture);
                }
            }

            .build()
        return notification
    }

    override fun onDestroy() {
        super.onDestroy()
        //cancel job on service Destroy
        job.cancel()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    companion object {
        const val CHANNEL_ID = "Download_Service"
    }
}

fun Context.startDownloadService(url: String) = run {
    Intent(this, ForegroundService::class.java).apply {
        putExtra("url", url)
    }.also { service ->
        ContextCompat.startForegroundService(this, service)
    }
}

