package soup.movie.ui.map

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import soup.movie.R
import soup.movie.data.model.Theater
import soup.movie.databinding.TheaterMapFragmentBinding
import soup.movie.ui.base.BaseFragment
import soup.movie.ui.base.OnBackPressedListener
import soup.movie.ui.main.MainViewModel
import soup.movie.util.*
import soup.movie.util.helper.Cgv
import soup.movie.util.helper.LotteCinema
import soup.movie.util.helper.Megabox
import kotlin.math.max
import kotlin.math.min

class TheaterMapFragment : BaseFragment(), OnBackPressedListener {

    private lateinit var binding: TheaterMapFragmentBinding

    private val activityViewModel: MainViewModel by activityViewModels()
    private val viewModel: TheaterMapViewModel by viewModels()

    private lateinit var locationSource: FusedLocationSource

    private val markers = arrayListOf<Marker>()

    private var selectedTheater: Theater? = null

    private var infoPanel: BottomSheetBehavior<out View>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TheaterMapFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.initViewState(viewModel)
        binding.adaptSystemWindowInset()
        return binding.root
    }

    override fun onDestroyView() {
        clearMarkers()
        super.onDestroyView()
    }

    private fun TheaterMapFragmentBinding.initViewState(viewModel: TheaterMapViewModel) {
        header.apply {
            toolbar.setNavigationOnClickListener {
                activityViewModel.openNavigationMenu()
            }
        }

        locationSource = FusedLocationSource(this@TheaterMapFragment, LOCATION_PERMISSION_REQUEST_CODE)
        val mapFragment = childFragmentManager.findFragmentById(R.id.naverMapFragment) as MapFragment
        mapFragment.getMapAsync { naverMap ->
            naverMap.mapType = NaverMap.MapType.Navi
            naverMap.isNightModeEnabled = isDarkTheme
            naverMap.locationSource = locationSource
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
            naverMap.setOnMapClickListener { _, _ ->
                naverMap.run {
                    moveCamera(
                        CameraUpdate
                            .zoomTo(min(cameraPosition.zoom, 12.0))
                            .animate(CameraAnimation.Easing))
                }
                hideInfoPanel()
            }
            viewModel.uiModel.observe(viewLifecycleOwner) {
                naverMap.render(it)
            }
            viewModel.onRefresh()
        }

        footer.apply {
            infoPanel = BottomSheetBehavior.from(root).apply {
                infoView.setOnDebounceClickListener { hideInfoPanel() }
                state = STATE_HIDDEN
            }
            navigationButton.setOnDebounceClickListener {
                selectedTheater?.toMapIntent()?.run {
                    it.context.startActivitySafely(this)
                }
            }
            infoButton.setOnDebounceClickListener {
                selectedTheater?.executeWeb(it.context)
            }
        }
    }

    private fun Theater.executeWeb(ctx: Context) {
        return when (type) {
            Theater.TYPE_CGV -> Cgv.executeWeb(ctx, this)
            Theater.TYPE_LOTTE -> LotteCinema.executeWeb(ctx, this)
            Theater.TYPE_MEGABOX -> Megabox.executeWeb(ctx, this)
            else -> throw IllegalArgumentException("$type is not valid type.")
        }
    }

    private fun TheaterMapFragmentBinding.adaptSystemWindowInset() {
        theaterMapScene.doOnApplyWindowInsets { view, windowInsets, initialPadding ->
            view.updatePadding(
                top = initialPadding.top + windowInsets.systemWindowInsetTop,
                bottom = initialPadding.bottom + windowInsets.systemWindowInsetBottom
            )
            footer.windowInsetBottomView.updateLayoutParams {
                height = initialPadding.bottom + windowInsets.systemWindowInsetBottom
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return hideInfoPanel()
    }

    private fun showInfoPanel(theater: Theater): Boolean {
        if (infoPanel?.state == STATE_HIDDEN) {
            infoPanel?.state = STATE_COLLAPSED
        }
        binding.footer.nameView.text = theater.fullName()
        selectedTheater = theater
        return true
    }

    private fun hideInfoPanel(): Boolean {
        if (infoPanel?.state != STATE_HIDDEN) {
            infoPanel?.state = STATE_HIDDEN
            selectedTheater = null
            return true
        }
        return false
    }

    private fun NaverMap.render(uiModel: TheaterMapUiModel) {
        if (uiModel is TheaterMapUiModel.DoneState) {
            clearMarkers()
            showMarkers(this, uiModel.myTheaters)
        }
    }

    private fun showMarkers(naverMap: NaverMap, theaters: List<Theater>) {
        markers.addAll(theaters.map(::createMarker))
        markers.forEach {
            it.map = naverMap
        }
    }

    private fun clearMarkers() {
        markers.forEach {
            it.map = null
        }
    }

    private fun createMarker(theater: Theater) = Marker().apply {
        captionText = theater.fullName()
        position = theater.position()
        icon = OverlayImage.fromResource(theater.getMarkerIcon())
        isHideCollidedSymbols = true
        isHideCollidedCaptions = true
        setOnClickListener {
            map?.run {
                moveCamera(
                    CameraUpdate
                        .scrollAndZoomTo(
                            position,
                            max(cameraPosition.zoom, 16.0)
                        )
                        .animate(CameraAnimation.Fly))
            }
            showInfoPanel(theater)
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}
