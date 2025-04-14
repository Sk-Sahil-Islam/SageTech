package com.example.sagetech

import android.accessibilityservice.AccessibilityService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.core.app.NotificationCompat
import kotlin.math.abs

class DoomScrollAccessibilityService : AccessibilityService() {

    companion object {
        var totalScrollDistance = 0
        private var lastScrollY = 0
    }

    override fun onServiceConnected() {
        super.onServiceConnected()

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
        Log.e("DoomScrollService", "Received event type: ${event?.eventType}")
        if (event?.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            // Get the current scroll position
            val currentScrollY = event.scrollY

            // Calculate the delta (how much was scrolled since last event)
            val delta = if (lastScrollY > 0) {
                // Only calculate delta if we have a previous value
                // Take absolute value to count both up and down scrolling
                abs(currentScrollY - lastScrollY)
            } else {
                // First scroll event
                0
            }

            // Update the last scroll position for next calculation
            lastScrollY = currentScrollY

            // Only update if we have a meaningful delta
            if (delta > 0 && delta < 10000) { // Ignore unreasonably large jumps
                totalScrollDistance += delta
                Log.e("DoomScrollService", "Scroll delta: $delta, Total: $totalScrollDistance")

                // Update the overlay
                OverlayWindowManager.updateOverlay(totalScrollDistance)
            }
        }
    }

    override fun onInterrupt() {
        // Handle interruption
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up the overlay when the service is destroyed
        OverlayWindowManager.removeOverlay()
    }

}