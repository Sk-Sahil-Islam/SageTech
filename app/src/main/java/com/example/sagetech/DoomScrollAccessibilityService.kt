package com.example.sagetech

import android.accessibilityservice.AccessibilityService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.core.app.NotificationCompat
import kotlin.math.abs

class DoomScrollAccessibilityService : AccessibilityService() {

    companion object {
        var totalScrollDistance = 0
        private var lastScrollY = -1
        private const val MAX_JUMP = 5000
        private const val SAVE_INTERVAL_MS = 5000L // Save every 5 seconds
    }

    private val saveHandler = Handler(Looper.getMainLooper())
    private val saveRunnable = object : Runnable {
        override fun run() {
            OverlayWindowManager.saveScrollDistance(this@DoomScrollAccessibilityService, totalScrollDistance)
            saveHandler.postDelayed(this, SAVE_INTERVAL_MS)
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()

        // Load saved scroll distance
        totalScrollDistance = OverlayWindowManager.getSavedScrollDistance(this)

        // Start periodic saving
        saveHandler.postDelayed(saveRunnable, SAVE_INTERVAL_MS)

        // Create a notification channel (required for Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "doom_scroll_channel",
                "Doom Scroll Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // Create a notification for the foreground service
        val notification = NotificationCompat.Builder(this, "doom_scroll_channel")
            .setContentTitle("Doom Scroll")
            .setContentText("Tracking your scroll distance")
//            .setSmallIcon(R.drawable.ic_notification) // Make sure you have this icon
            .build()

        // Start as a foreground service
        startForeground(1001, notification)

        // Initialize the overlay
        OverlayWindowManager.initialize(this)
        Log.e("DoomScrollService", "Service started in foreground")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.takeIf { it.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED }?.let { ev ->
            // Instead of relying on scrollY, use the fromIndex and toIndex from the event
            // or use a pixel-based estimation

            // Method 1: Use itemCount if available (for RecyclerView/ListView scrolling)
            val itemCount = ev.itemCount
            val fromIndex = ev.fromIndex
            val toIndex = ev.toIndex

            // Calculate an estimated scroll distance
            var delta = 0

            if (itemCount > 0 && fromIndex >= 0 && toIndex >= 0) {
                // For list-based scrolling, estimate based on items scrolled
                val itemsScrolled = abs(toIndex - fromIndex)
                delta = itemsScrolled * 50 // Assume average item height of 50px
                Log.d("ScrollDebug", "List scroll: from=$fromIndex, to=$toIndex, items=$itemsScrolled")
            } else {
                // For pixel-based scrolling, use a fixed increment
                // This is a fallback when we can't get precise measurements
                delta = 20 // Assume a standard small scroll increment
                Log.d("ScrollDebug", "Generic scroll detected, using default increment")
            }

            // Apply the delta with the same constraints as before
            delta = delta.coerceAtMost(MAX_JUMP)

            if (delta > 0) {
                totalScrollDistance += delta
                Log.d("ScrollTrack", "Delta: $delta, Total: $totalScrollDistance")

                Handler(Looper.getMainLooper()).post {
                    OverlayWindowManager.updateOverlay(totalScrollDistance)
                }
            }
        }
    }



    override fun onInterrupt() {
        // Handle interruption
    }

    override fun onDestroy() {
        super.onDestroy()
        // Save final value and remove callbacks
        OverlayWindowManager.saveScrollDistance(this, totalScrollDistance)
        saveHandler.removeCallbacks(saveRunnable)
        OverlayWindowManager.removeOverlay()
    }

}