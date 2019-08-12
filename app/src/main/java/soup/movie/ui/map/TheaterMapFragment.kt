package soup.movie.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.util.FusedLocationSource
import soup.movie.R
import soup.movie.databinding.TheaterMapFragmentBinding
import soup.movie.ui.base.BaseFragment
import soup.movie.ui.main.MainViewModel
import soup.movie.util.doOnApplyWindowInsets

class TheaterMapFragment : BaseFragment() {

    private lateinit var binding: TheaterMapFragmentBinding

    private val activityViewModel: MainViewModel by activityViewModels()
    private val viewModel: TheaterMapViewModel by viewModels()

    private lateinit var locationSource: FusedLocationSource

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TheaterMapFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.initViewState()
        binding.adaptSystemWindowInset()
        return binding.root
    }

    private fun TheaterMapFragmentBinding.initViewState() {
        header.apply {
            toolbar.setNavigationOnClickListener {
                activityViewModel.openNavigationMenu()
            }
        }

        locationSource = FusedLocationSource(this@TheaterMapFragment, LOCATION_PERMISSION_REQUEST_CODE)
        val mapFragment = childFragmentManager.findFragmentById(R.id.naverMapFragment) as MapFragment
        mapFragment.getMapAsync { naverMap ->
            naverMap.locationSource = locationSource
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        }
    }

    private fun TheaterMapFragmentBinding.adaptSystemWindowInset() {
        theaterMapScene.doOnApplyWindowInsets { view, windowInsets, initialPadding ->
            view.updatePadding(
                top = initialPadding.top + windowInsets.systemWindowInsetTop,
                bottom = initialPadding.bottom + windowInsets.systemWindowInsetBottom
            )
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
