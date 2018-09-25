package soup.movie.ui.main.theaters

import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
import soup.movie.ui.BaseContract

abstract class LocationMapFragment<V: BaseContract.View, P: BaseContract.Presenter<V>> :
        MapFragment<V, P>(), PermissionsListener {

    private var locationLayerPlugin: LocationLayerPlugin? = null

    private lateinit var permissionsManager: PermissionsManager

    override fun onResume() {
        super.onResume()
        if (!PermissionsManager.areLocationPermissionsGranted(context)) {
            locationLayerPlugin?.run{
                lifecycle.removeObserver(this)
            }
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        super.onMapReady(mapboxMap)
        enableLocationPlugin()
    }

    private fun enableLocationPlugin() {
        if (PermissionsManager.areLocationPermissionsGranted(context)) {
            locationLayerPlugin = LocationLayerPlugin(mapView, mapboxMap).apply {
                cameraMode = CameraMode.TRACKING
                renderMode = RenderMode.NORMAL
                lifecycle.addObserver(this)
            }
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(activity)
        }
        locationLayerPlugin?.run {
            lifecycle.addObserver(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>) {
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocationPlugin()
        }
    }

    protected fun setCameraTracking(enabled: Boolean) {
        locationLayerPlugin?.cameraMode = when (enabled) {
            true -> CameraMode.TRACKING
            false -> CameraMode.NONE
        }
    }
}
