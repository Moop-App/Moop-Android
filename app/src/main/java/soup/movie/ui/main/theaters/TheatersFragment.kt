package soup.movie.ui.main.theaters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
import kotlinx.android.synthetic.main.fragment_theaters.*
import soup.movie.data.helper.fullName
import soup.movie.data.helper.getSelectedMarkerIcon
import soup.movie.data.helper.position
import soup.movie.data.model.Theater
import soup.movie.databinding.FragmentTheatersBinding
import soup.movie.ui.main.BaseTabFragment
import soup.movie.util.Interpolators
import soup.movie.util.loadIconOrDefault
import soup.movie.util.log.printRenderLog
import soup.movie.util.showToast
import javax.inject.Inject
import kotlin.math.max

class TheatersFragment :
        BaseTabFragment<TheatersContract.View, TheatersContract.Presenter>(),
        TheatersContract.View, PermissionsListener {

    @Inject
    override lateinit var presenter: TheatersContract.Presenter

    private lateinit var mapboxMap: MapboxMap
    
    private var locationLayerPlugin: LocationLayerPlugin? = null
    private lateinit var permissionsManager: PermissionsManager

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            FragmentTheatersBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { it ->
            // Trick: Fake scene animation
            dim.animateHide()

            mapboxMap = it
            mapboxMap.setOnMarkerClickListener { marker ->
                context?.showToast(marker.title)
                mapboxMap.animateCamera {
                    CameraPosition.Builder()
                            .target(marker.position)
                            .zoom(max(it.cameraPosition.zoom, 16.0))
                            .build()
                }
                true
            }
            enableLocationPlugin()
            presenter.onMapReady()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            dim.animateShow()
        } else {
            dim.animateHide()
        }
    }

    private fun View.animateHide() {
        animate().cancel()
        alpha = 1f
        animate()
                .alpha(0f)
                .setDuration(300)
                .setInterpolator(Interpolators.ALPHA_OUT)
                .setStartDelay(200)
                .withEndAction { visibility = View.INVISIBLE }
    }

    private fun View.animateShow() {
        animate().cancel()
        alpha = 0f
        visibility = View.VISIBLE
        animate()
                .alpha(1f)
                .setDuration(200)
                .setInterpolator(Interpolators.ACCELERATE_DECELERATE)
                .setStartDelay(0)
                // We need to clean up any pending end action from animateHide if we call
                // both hide and show in the same frame before the animation actually gets started.
                // cancel() doesn't really remove the end action.
                .withEndAction(null)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()

        if (!PermissionsManager.areLocationPermissionsGranted(context)) {
            locationLayerPlugin?.run{
                lifecycle.removeObserver(this)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (!mapView.isDestroyed) {
            mapView.onSaveInstanceState(outState)
        }
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        if (mapView != null && !mapView.isDestroyed) {
            mapView.onLowMemory()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun render(viewState: TheatersViewState) {
        printRenderLog { viewState }
        mapboxMap.addMarkers(viewState.myTheaters.map { it.toMarker(mapView.context) })
    }

    private fun Theater.toMarker(context: Context): MarkerOptions = MarkerOptions()
            .setTitle(fullName())
            .setPosition(position())
            .setIcon(context.loadIconOrDefault(getSelectedMarkerIcon()))

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

    companion object {

        fun newInstance(): TheatersFragment = TheatersFragment()
    }
}
