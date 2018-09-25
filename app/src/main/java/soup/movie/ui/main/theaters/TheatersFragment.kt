package soup.movie.ui.main.theaters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
import kotlinx.android.synthetic.main.fragment_theaters.*
import soup.movie.data.helper.*
import soup.movie.data.model.Theater
import soup.movie.databinding.FragmentTheatersBinding
import soup.movie.ui.BaseFragment
import soup.movie.ui.main.BaseTabFragment
import soup.movie.ui.main.theaters.TheatersViewState.DoneState
import soup.movie.ui.main.theaters.TheatersViewState.ErrorState
import soup.movie.util.*
import soup.movie.util.log.printRenderLog
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class TheatersFragment :
        BaseTabFragment<TheatersContract.View, TheatersContract.Presenter>(),
        BaseFragment.OnBackListener,
        BaseTabFragment.OnReselectListener,
        TheatersContract.View, PermissionsListener {

    @Inject
    override lateinit var presenter: TheatersContract.Presenter

    private lateinit var mapboxMap: MapboxMap
    
    private var locationLayerPlugin: LocationLayerPlugin? = null
    private lateinit var permissionsManager: PermissionsManager

    private var selectedTheater: Theater? = null

    private val infoPanel by lazy {
        BottomSheetBehavior.from(infoView).apply {
            infoView.setOnClickListener { hideInfoPanel() }
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            FragmentTheatersBinding.inflate(inflater, container, false).root

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        infoPanel.state = STATE_HIDDEN
        navigationButton.setOnClickListener {
            selectedTheater?.toMapIntent()?.run {
                it.context.startActivitySafely(this)
            }
        }
        infoButton.setOnClickListener {
            selectedTheater?.toDetailWebUrl()?.run {
                context?.executeWebPage(this)
            }
        }
        errorView.setOnClickListener {
            presenter.refresh()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { it ->
            // Trick: Fake scene animation
            dim.animateHide()

            mapboxMap = it
            mapboxMap.setLatLngBoundsForCameraTarget(KOREA_BOUNDS)
            mapboxMap.setOnMarkerClickListener { marker ->
                mapboxMap.animateCamera {
                    setCameraTracking(false)
                    CameraPosition.Builder()
                            .target(marker.position)
                            .zoom(max(it.cameraPosition.zoom, 16.0))
                            .build()
                }
                marker.snippet.fromJson<Theater>()?.run { showInfoPanel(this) }
                true
            }
            mapboxMap.addOnMapClickListener { hideInfoPanel() }
            enableLocationPlugin()
            presenter.refresh()
        }
    }

    private fun showInfoPanel(theater: Theater): Boolean {
        if (infoPanel.state == STATE_HIDDEN) {
            infoPanel.state = STATE_COLLAPSED
            nameView.text = theater.fullName()
            selectedTheater = theater
            return true
        }
        return false
    }

    private fun hideInfoPanel(): Boolean {
        if (infoPanel.state != STATE_HIDDEN) {
            infoPanel.state = STATE_HIDDEN
            mapboxMap.animateCamera {
                CameraUpdateFactory
                        .zoomTo(min(it.cameraPosition.zoom, 12.0))
                        .getCameraPosition(mapboxMap)
            }
            selectedTheater = null
            return true
        }
        return false
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
        errorView?.setVisibleIf { viewState is ErrorState }
        if (viewState is DoneState) {
            mapboxMap.addMarkers(viewState.myTheaters
                    .map { it.toMarker(mapView.context) })
        }
    }

    private fun Theater.toMarker(context: Context): MarkerOptions = MarkerOptions()
            .setTitle(fullName())
            .setPosition(position())
            .setIcon(context.loadIconOrDefault(getSelectedMarkerIcon()))
            .setSnippet(toJson())

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

    private fun setCameraTracking(enabled: Boolean) {
        locationLayerPlugin?.cameraMode = when (enabled) {
            true -> CameraMode.TRACKING
            false -> CameraMode.NONE
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

    override fun onBackPressed(): Boolean = hideInfoPanel()

    override fun onReselect() {
        if (!hideInfoPanel()) {
            setCameraTracking(true)
        }
    }

    companion object {

        private val KOREA_BOUNDS = LatLngBounds.Builder()
                .include(LatLng(31.43, 122.37))
                .include(LatLng(44.35, 132.0))
                .build()

        fun newInstance(): TheatersFragment = TheatersFragment()
    }
}
