package soup.movie.ui.map

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.lifecycle.observe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import soup.movie.BuildConfig
import soup.movie.R
import soup.movie.databinding.TheaterMapFragmentBinding
import soup.movie.ext.*
import soup.movie.system.SystemViewModel
import soup.movie.ui.base.OnBackPressedListener
import soup.movie.util.LauncherIcons
import soup.movie.util.helper.Cgv
import soup.movie.util.helper.LotteCinema
import soup.movie.util.helper.Megabox
import soup.movie.util.setOnDebounceClickListener
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class TheaterMapFragment : BaseMapFragment(), OnBackPressedListener {

    private lateinit var binding: TheaterMapFragmentBinding

    @Inject
    lateinit var systemViewModelFactory: SystemViewModel.Factory
    private val systemViewModel: SystemViewModel by assistedActivityViewModels {
        systemViewModelFactory.create()
    }

    @Inject
    lateinit var theaterMapFactory: TheaterMapViewModel.Factory
    private val viewModel: TheaterMapViewModel by assistedViewModels {
        theaterMapFactory.create()
    }

    private lateinit var locationSource: FusedLocationSource

    private val markers = arrayListOf<Marker>()

    private var selectedTheater: TheaterMarkerUiModel? = null

    private var infoPanel: BottomSheetBehavior<out View>? = null

    private val launcherIcons by lazyFast {
        LauncherIcons(requireContext())
    }

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
        hideInfoPanel()
        clearMarkers()
        super.onDestroyView()
    }

    private fun TheaterMapFragmentBinding.initViewState(viewModel: TheaterMapViewModel) {
        header.apply {
            toolbar.setNavigationOnClickListener {
                systemViewModel.openNavigationMenu()
            }
        }

        locationSource = FusedLocationSource(this@TheaterMapFragment, LOCATION_PERMISSION_REQUEST_CODE)
        setMapView(contents.mapView)
        getMapAsync { naverMap ->
            if (isDarkTheme) {
                naverMap.mapType = NaverMap.MapType.Navi
                naverMap.isNightModeEnabled = true
                mapCover.setBackgroundColor(NaverMap.DEFAULT_BACKGROUND_COLOR_DARK)
            } else {
                naverMap.mapType = NaverMap.MapType.Basic
                naverMap.isNightModeEnabled = false
                mapCover.setBackgroundColor(NaverMap.DEFAULT_BACKGROUND_COLOR_LIGHT)
            }
            naverMap.locationSource = locationSource
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
            naverMap.moveCamera(CameraUpdate.zoomTo(12.0))
            naverMap.setOnMapClickListener { _, _ ->
                hideInfoPanel()
            }
            viewModel.uiModel.observe(viewLifecycleOwner) {
                naverMap.render(it)
                mapCover.animateGone(isGone = true, startDelay = 500)
            }
            viewModel.onRefresh()
        }

        footer.apply {
            infoPanel = BottomSheetBehavior.from(root).apply {
                state = STATE_HIDDEN
            }
            infoView.setOnDebounceClickListener {
                hideInfoPanel()
            }
            googleMapButton.apply {
                val packageName = "com.google.android.apps.maps"
                val appIcon = launcherIcons.getAppIcon(context, packageName)
                setImageDrawable(appIcon)
                isVisible = appIcon != null
                setOnDebounceClickListener {
                    selectedTheater?.run {
                        val gmmIntentUri = Uri.parse("geo:$lat,$lng?q=${Uri.encode(name)}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage(packageName)
                        startActivity(mapIntent)
                    }
                }
            }
            naverMapButton.apply {
                val packageName = "com.nhn.android.nmap"
                val appIcon = launcherIcons.getAppIcon(context, packageName)
                setImageDrawable(appIcon)
                isVisible = appIcon != null
                setOnDebounceClickListener {
                    selectedTheater?.run {
                        val gmmIntentUri = Uri.parse("nmap://place?lat=$lat&lng=$lng&name=${Uri.encode(name)}&appname=${BuildConfig.APPLICATION_ID}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage(packageName)
                        startActivity(mapIntent)
                    }
                }
            }
            kakaoMapButton.apply {
                val packageName = "net.daum.android.map"
                val appIcon = launcherIcons.getAppIcon(context, packageName)
                setImageDrawable(appIcon)
                isVisible = appIcon != null
                setOnDebounceClickListener {
                    selectedTheater?.run {
                        val gmmIntentUri = Uri.parse("geo:$lat,$lng?q=${Uri.encode(name)}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage(packageName)
                        startActivity(mapIntent)
                    }
                }
            }
            infoButton.setOnDebounceClickListener {
                selectedTheater?.executeWeb(it.context)
            }
        }
    }

    private fun TheaterMapFragmentBinding.adaptSystemWindowInset() {
        theaterMapScene.doOnApplyWindowInsets { theaterMapScene, insets, initialState ->
            theaterMapScene.updatePadding(
                top = initialState.paddings.top + insets.systemWindowInsetTop,
                bottom = initialState.paddings.bottom + insets.systemWindowInsetBottom
            )
        }
        footer.windowInsetBottomView.doOnApplyWindowInsets { windowInsetBottomView, insets, initialState ->
            windowInsetBottomView.updateLayoutParams {
                height = initialState.paddings.bottom + insets.systemWindowInsetBottom
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return hideInfoPanel()
    }

    private fun showInfoPanel(theater: TheaterMarkerUiModel): Boolean {
        if (infoPanel?.state == STATE_HIDDEN) {
            infoPanel?.state = STATE_COLLAPSED
        }
        binding.footer.nameView.text = theater.name
        selectedTheater = theater
        return true
    }

    private fun hideInfoPanel(): Boolean {
        if (infoPanel?.state != STATE_HIDDEN) {
            infoPanel?.state = STATE_HIDDEN
            selectedTheater = null

            getMapAsync {
                it.moveCamera(
                    CameraUpdate
                        .zoomTo(min(it.cameraPosition.zoom, 12.0))
                        .animate(CameraAnimation.Easing))
            }
            return true
        }
        return false
    }

    private fun NaverMap.render(uiModel: TheaterMapUiModel) {
        clearMarkers()
        showMarkers(this, uiModel.theaterMarkerList)
    }

    private fun showMarkers(naverMap: NaverMap, theaters: List<TheaterMarkerUiModel>) {
        theaters.map(::createMarker).forEach {
            markers += it.apply {
                map = naverMap
            }
        }
    }

    private fun clearMarkers() {
        markers.forEach {
            it.map = null
        }
        markers.clear()
    }

    private fun createMarker(theater: TheaterMarkerUiModel) = Marker().apply {
        captionText = theater.name
        position = LatLng(theater.lat, theater.lng)
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

    @DrawableRes
    private fun TheaterMarkerUiModel.getMarkerIcon(): Int {
        return when (this) {
            is CgvMarkerUiModel -> R.drawable.ic_marker_cgv
            is LotteCinemaMarkerUiModel -> R.drawable.ic_marker_lotte
            is MegaboxMarkerUiModel -> R.drawable.ic_marker_megabox
        }
    }

    private fun TheaterMarkerUiModel.executeWeb(ctx: Context) {
        return when (this) {
            is CgvMarkerUiModel ->
                Cgv.executeWeb(ctx, this)
            is LotteCinemaMarkerUiModel ->
                LotteCinema.executeWeb(ctx, this)
            is MegaboxMarkerUiModel ->
                Megabox.executeWeb(ctx, this)
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
