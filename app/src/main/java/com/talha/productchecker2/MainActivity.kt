package com.talha.productchecker2

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var startButton: Button
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById(R.id.startButton)
        resultTextView = findViewById(R.id.resultTextView)

        startButton.setOnClickListener {
            // the ProductCheckerWorker to run every 15 minutes (adjust as needed)
            scheduleProductCheckerWorker()
        }
    }

    private fun scheduleProductCheckerWorker() {
        val workRequest = PeriodicWorkRequest.Builder(
            ProductCheckerWorker::class.java,
            15, // at least must have 15 minutes
            TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }
}
