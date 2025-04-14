import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import com.example.sagetech.DoomScrollMode
import com.example.sagetech.DoomScrollOverlay

object OverlayWindowManager {
    private var windowManager: WindowManager? = null
    private var overlayView: ComposeView? = null
    private var isOverlayVisible = true

    // Create a combined LifecycleOwner and SavedStateRegistryOwner
    private val viewModelStoreOwner = object : LifecycleOwner, SavedStateRegistryOwner {
        private val lifecycleRegistry = LifecycleRegistry(this)
        private val savedStateRegistryController = SavedStateRegistryController.create(this)

        init {
            savedStateRegistryController.performRestore(null)
            lifecycleRegistry.currentState = Lifecycle.State.RESUMED
        }

        override val lifecycle: Lifecycle
            get() = lifecycleRegistry
        override val savedStateRegistry: SavedStateRegistry
            get() = savedStateRegistryController.savedStateRegistry
    }

    fun initialize(context: Context) {
        if (windowManager != null) return
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        // Create a ComposeView programmatically
        overlayView = ComposeView(context).apply {
            // Set the composition strategy
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)

            try {
                // Set the LifecycleOwner
                val viewTreeLifecycleOwnerClass = Class.forName("androidx.lifecycle.ViewTreeLifecycleOwner")
                val setLifecycleMethod = viewTreeLifecycleOwnerClass.getMethod("set", android.view.View::class.java, LifecycleOwner::class.java)
                setLifecycleMethod.invoke(null, this, viewModelStoreOwner)

                // Set the SavedStateRegistryOwner
                val viewTreeSavedStateRegistryOwnerClass = Class.forName("androidx.savedstate.ViewTreeSavedStateRegistryOwner")
                val setSavedStateMethod = viewTreeSavedStateRegistryOwnerClass.getMethod("set", android.view.View::class.java, SavedStateRegistryOwner::class.java)
                setSavedStateMethod.invoke(null, this, viewModelStoreOwner)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            setContent { DoomScrollOverlay(scrollDistance = 0) }
        }

        // Set up window layout parameters for the overlay
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            x = 0
            y = 100 // Adjust the position as needed
        }

        // Add the ComposeView overlay to the window
        try {
            windowManager?.addView(overlayView, params)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Update the overlay by setting its Compose content
    fun updateOverlay(scrollDistance: Int) {
        try {
            overlayView?.let { view ->
                // Use main thread to update the UI
                Handler(Looper.getMainLooper()).post {
                    view.setContent {
                        DoomScrollOverlay(scrollDistance = scrollDistance)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("OverlayWindowManager", "Failed to update overlay", e)
        }
    }
    fun toggleOverlayVisibility() {
        overlayView?.let { view ->
            isOverlayVisible = !isOverlayVisible
            Handler(Looper.getMainLooper()).post {
                view.visibility = if (isOverlayVisible) {
                    android.view.View.VISIBLE
                } else {
                    android.view.View.GONE
                }
            }
            Log.d("OverlayWindowManager", "Overlay visibility changed to: $isOverlayVisible")
        }
    }
    fun removeOverlay() {
        if (overlayView != null) {
            // Get the lifecycle registry and update its state
            (viewModelStoreOwner.lifecycle as? LifecycleRegistry)?.currentState = Lifecycle.State.DESTROYED
            windowManager?.removeView(overlayView)
            overlayView = null
        }
    }

    // Add this function to OverlayWindowManager
    fun resetScrollDistance(context: Context) {
        // Clear the preference value
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putInt(KEY_SCROLL_DISTANCE, 0)
            .commit() // Use commit() instead of apply() for immediate write

        // Update the overlay to show zero distance
        updateOverlay(0)

        // Log the reset action
        Log.d("OverlayWindowManager", "Scroll distance has been reset to 0")
    }

    // Add this function to OverlayWindowManager
    fun updateOverlayMode(mode: DoomScrollMode) {
        try {
            overlayView?.let { view ->
                // Use main thread to update the UI
                Handler(Looper.getMainLooper()).post {
                    view.setContent {
                        // Get the current scroll distance
                        val scrollDistance = getSavedScrollDistance(view.context)
                        DoomScrollOverlay(scrollDistance = scrollDistance, mode = mode)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("OverlayWindowManager", "Failed to update overlay mode", e)
        }
    }

    // Add SharedPreferences to persist scroll distance
    private const val PREFS_NAME = "doom_scroll_prefs"
    private const val KEY_SCROLL_DISTANCE = "total_scroll_distance"

    // Load saved scroll distance
    fun getSavedScrollDistance(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_SCROLL_DISTANCE, 0)
    }

    // Save current scroll distance
    fun saveScrollDistance(context: Context, scrollDistance: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_SCROLL_DISTANCE, scrollDistance).apply()
    }

}