package soup.movie.ui.main.theaters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.maps.MapboxMap
import kotlinx.android.synthetic.main.fragment_theaters.*
import soup.movie.data.helper.position
import soup.movie.data.model.Theater
import soup.movie.databinding.FragmentTheatersBinding
import soup.movie.ui.main.BaseTabFragment
import soup.movie.util.log.printRenderLog
import javax.inject.Inject

class TheatersFragment :
        BaseTabFragment<TheatersContract.View, TheatersContract.Presenter>(),
        TheatersContract.View {

    @Inject
    override lateinit var presenter: TheatersContract.Presenter

    private lateinit var mapboxMap: MapboxMap

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            FragmentTheatersBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            mapboxMap = it
            presenter.onMapReady()
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
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
        mapboxMap.addMarkers(viewState.myTheaters.map { it.toMarker() })
    }

    private fun Theater.toMarker(): MarkerOptions = MarkerOptions()
            .setPosition(position())
            .setIcon(IconFactory.getInstance(context!!).defaultMarkerView())

    companion object {

        fun newInstance(): TheatersFragment = TheatersFragment()
    }
}
