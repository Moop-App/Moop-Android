package soup.movie.ui.map

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
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
import soup.movie.BuildConfig
import soup.movie.R
import soup.movie.data.model.Theater
import soup.movie.databinding.TheaterMapFragmentBinding
import soup.movie.ui.base.BaseMapFragment
import soup.movie.ui.base.OnBackPressedListener
import soup.movie.ui.main.MainViewModel
import soup.movie.util.*
import soup.movie.util.helper.Cgv
import soup.movie.util.helper.LotteCinema
import soup.movie.util.helper.Megabox
import timber.log.Timber
import kotlin.math.max
import kotlin.math.min

class TheaterMapFragment : BaseMapFragment(), OnBackPressedListener {

    private lateinit var binding: TheaterMapFragmentBinding

    private val activityViewModel: MainViewModel by activityViewModels()
    private val viewModel: TheaterMapViewModel by viewModels()

    private lateinit var locationSource: FusedLocationSource

    private val markers = arrayListOf<Marker>()

    private var selectedTheater: Theater? = null

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
                activityViewModel.openNavigationMenu()
            }
        }

        locationSource = FusedLocationSource(this@TheaterMapFragment, LOCATION_PERMISSION_REQUEST_CODE)
        setMapView(contents.mapView)
        getMapAsync { naverMap ->
            if (isDarkTheme) {
                naverMap.mapType = NaverMap.MapType.Navi
                naverMap.isNightModeEnabled = true
            } else {
                naverMap.mapType = NaverMap.MapType.Basic
                naverMap.isNightModeEnabled = false
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
                val appIcon = context.loadAppIcon(packageName)
                setImageDrawable(appIcon)
                isVisible = appIcon != null
                setOnDebounceClickListener {
                    selectedTheater?.run {
                        val gmmIntentUri = Uri.parse("geo:$lat,$lng?q=${Uri.encode(fullName())}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage(packageName)
                        startActivity(mapIntent)
                    }
                }
            }
            naverMapButton.apply {
                val packageName = "com.nhn.android.nmap"
                val appIcon = context.loadAppIcon(packageName)
                setImageDrawable(appIcon)
                isVisible = appIcon != null
                setOnDebounceClickListener {
                    selectedTheater?.run {
                        val gmmIntentUri = Uri.parse("nmap://place?lat=$lat&lng=$lng&name=${Uri.encode(fullName())}&appname=${BuildConfig.APPLICATION_ID}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage(packageName)
                        startActivity(mapIntent)
                    }
                }
            }
            kakaoMapButton.apply {
                val packageName = "net.daum.android.map"
                val appIcon = context.loadAppIcon(packageName)
                setImageDrawable(appIcon)
                isVisible = appIcon != null
                setOnDebounceClickListener {
                    selectedTheater?.run {
                        val gmmIntentUri = Uri.parse("geo:$lat,$lng?q=${Uri.encode(fullName())}")
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
        if (uiModel is TheaterMapUiModel.DoneState) {
            clearMarkers()
            showMarkers(this, uiModel.myTheaters)
        }
    }

    private fun showMarkers(naverMap: NaverMap, theaters: List<Theater>) {
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

    @DrawableRes
    private fun Theater.getMarkerIcon(): Int {
        return when (type) {
            Theater.TYPE_CGV -> R.drawable.ic_marker_cgv
            Theater.TYPE_LOTTE -> R.drawable.ic_marker_lotte
            Theater.TYPE_MEGABOX -> R.drawable.ic_marker_megabox
            else -> throw IllegalArgumentException("$type is not valid type.")
        }
    }

    private fun Theater.position(): LatLng {
        return LatLng(lat, lng)
    }

    private fun Theater.fullName(): String {
        return when (type) {
            Theater.TYPE_CGV -> "CGV $name"
            Theater.TYPE_LOTTE -> "롯데시네마 $name"
            Theater.TYPE_MEGABOX -> "메가박스 $name"
            else -> name
        }
    }

    private fun Context.loadAppIcon(packageName: String): Drawable? {
        try {
            return packageManager.run {
                getApplicationInfo(packageName, 0)
                    .let(::getApplicationIcon)
                    .let(launcherIcons::getShadowedIcon)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.w(e, "Failed looking up ApplicationInfo for $packageName")
            return null
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
