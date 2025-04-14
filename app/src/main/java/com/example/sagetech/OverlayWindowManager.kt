import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
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
import com.example.sagetech.DoomScrollOverlay

object OverlayWindowManager {
    private var windowManager: WindowManager? = null
    private var overlayView: ComposeView? = null

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
            WindowManager.LayoutParams.WRAP_CONTENT,
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
        overlayView?.post {
            overlayView?.setContent {
                DoomScrollOverlay(scrollDistance = scrollDistance)
            }
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
}