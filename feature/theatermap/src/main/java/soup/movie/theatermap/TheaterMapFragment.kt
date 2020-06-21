package soup.movie.theatermap

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
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
import dagger.hilt.android.EntryPointAccessors
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import soup.movie.BuildConfig
import soup.movie.di.TheaterMapModuleDependencies
import soup.movie.ext.animateGone
import soup.movie.ext.isDarkTheme
import soup.movie.ext.lazyFast
import soup.movie.model.Theater
import soup.movie.model.Theater.Companion.TYPE_CGV
import soup.movie.model.Theater.Companion.TYPE_LOTTE
import soup.movie.model.Theater.Companion.TYPE_MEGABOX
import soup.movie.model.repository.MoopRepository
import soup.movie.system.SystemViewModel
import soup.movie.theatermap.databinding.TheaterMapFragmentBinding
import soup.movie.theatermap.di.DaggerTheaterMapComponent
import soup.movie.ui.base.OnBackPressedListener
import soup.movie.util.*
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class TheaterMapFragment : BaseMapFragment(R.layout.theater_map_fragment), OnBackPressedListener {

    @Inject
    lateinit var repository: MoopRepository

    private lateinit var binding: TheaterMapFragmentBinding

    private val systemViewModel: SystemViewModel by activityViewModels()
    private val viewModel: TheaterMapViewModel by viewModels {
        viewModelProviderFactoryOf { TheaterMapViewModel(repository) }
    }

    private lateinit var locationSource: FusedLocationSource

    private val markers = arrayListOf<Marker>()

    private var selectedTheater: TheaterMarkerUiModel? = null

    private var infoPanel: BottomSheetBehavior<out View>? = null

    private val launcherIcons by lazyFast {
        LauncherIcons(requireContext())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerTheaterMapComponent.builder()
            .context(context)
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    context.applicationContext,
                    TheaterMapModuleDependencies::class.java
                )
            )
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = TheaterMapFragmentBinding.bind(view).apply {
            initViewState(viewModel)
            adaptSystemWindowInset()
        }
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
                Cgv.executeWeb(ctx, Theater(TYPE_CGV, code, name, lng, lat))
            is LotteCinemaMarkerUiModel ->
                LotteCinema.executeWeb(ctx, Theater(TYPE_LOTTE, code, name, lng, lat))
            is MegaboxMarkerUiModel ->
                Megabox.executeWeb(ctx, Theater(TYPE_MEGABOX, code, name, lng, lat))
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
