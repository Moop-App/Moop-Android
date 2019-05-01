package soup.movie.ui.main.theaters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
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
        LocationMapFragment<TheatersContract.View, TheatersContract.Presenter>(),
        BaseFragment.OnBackListener,
        BaseTabFragment.OnReselectListener,
        TheatersContract.View {

    @Inject
    override lateinit var presenter: TheatersContract.Presenter

    private var selectedTheater: Theater? = null

    override val mapView: MapView
        get() = theaterMapView

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
        navigationButton.setOnDebounceClickListener {
            selectedTheater?.toMapIntent()?.run {
                it.context.startActivitySafely(this)
            }
        }
        infoButton.setOnDebounceClickListener {
            selectedTheater?.run {
                executeWeb(requireContext())
            }
        }
        errorView.setOnClickListener {
            presenter.refresh()
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        super.onMapReady(mapboxMap)

        // Trick: Fake scene animation
        dim.animateHide()

        this.mapboxMap = mapboxMap
        mapboxMap.setOnMarkerClickListener { marker ->
            moveCamera {
                CameraPosition.Builder()
                        .target(marker.position)
                        .zoom(max(it.cameraPosition.zoom, 16.0))
                        .build()
            }
            marker.snippet.fromJson<Theater>()?.run {
                showInfoPanel(this)
            }
            true
        }
        mapboxMap.addOnMapClickListener { hideInfoPanel() }
        presenter.refresh()
    }

    private fun showInfoPanel(theater: Theater): Boolean {
        if (infoPanel.state == STATE_HIDDEN) {
            infoPanel.state = STATE_COLLAPSED
        }
        nameView.text = theater.fullName()
        selectedTheater = theater
        return true
    }

    private fun hideInfoPanel(): Boolean {
        if (infoPanel.state != STATE_HIDDEN) {
            infoPanel.state = STATE_HIDDEN
            moveCamera {
                CameraUpdateFactory
                        .zoomTo(min(it.cameraPosition.zoom, 12.0))
                        .getCameraPosition(it)
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

    override fun render(viewState: TheatersViewState) {
        printRenderLog { viewState }
        errorView?.setVisibleIf { viewState is ErrorState }
        if (viewState is DoneState) {
            mapboxMap.addMarkers(viewState.myTheaters
                    .map { it.toMarker(theaterMapView.context) })
        }
    }

    private fun Theater.toMarker(context: Context): MarkerOptions = MarkerOptions()
            .setTitle(fullName())
            .setPosition(position())
            .setIcon(context.loadIconOrDefault(getSelectedMarkerIcon()))
            .setSnippet(toJson())

    override fun onBackPressed(): Boolean = hideInfoPanel()

    override fun onReselect() {
        if (!hideInfoPanel()) {
            trackMyLocation()
        }
    }

    companion object {

        fun newInstance(): TheatersFragment = TheatersFragment()
    }
}
