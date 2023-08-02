package com.talha.productchecker2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.jsoup.Jsoup

class ProductCheckerWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        try {
            val url = "https://order.mandarake.co.jp/order/listPage/list?categoryCode=02&lang=en"
            val document = Jsoup.connect(url).get()
            val productElements = document.select("div.block")

            for (element in productElements) {
                val productName = element.select("div.title").text()
                if (productName.contains("play arts", ignoreCase = true) ||
                    productName.contains("play arts kai", ignoreCase = true) ||
                    productName.contains("bring arts", ignoreCase = true)) {
                    sendNotification(productName)
                }
            }

            return Result.success()
        } catch (e: Exception) {
            Log.e("ProductChecker", "Error: ${e.message}")
            return Result.failure()
        }
    }

    private fun sendNotification(productName: String) {
        val channelId = "product_notification_channel"
        val notificationId = 1
        val context = applicationContext

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Product Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable._7501003_yan_0d2d91_1650x1650)
            .setContentTitle("New")
            .setContentText("$productName named .")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}







