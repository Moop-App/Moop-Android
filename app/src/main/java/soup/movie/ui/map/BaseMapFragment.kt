package soup.movie.ui.map

import android.os.Bundle
import android.view.View
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import dagger.android.support.DaggerFragment

abstract class BaseMapFragment : DaggerFragment() {

    private val callbacks = ArrayList<OnMapReadyCallback>()
    private var mapView: MapView? = null

    fun setMapView(mapView: MapView) {
        this.mapView = mapView
    }

    fun getMapView(): MapView? {
        return this.mapView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView?.run {
            onCreate(savedInstanceState)
            callbacks.forEach {
                getMapAsync(it)
            }
            callbacks.clear()
        }
    }

    override fun onStart() {
        super.onStart()
        this.mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        this.mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        this.mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        this.mapView?.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //this.mapView?.onDestroy()
        this.mapView = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        this.mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        this.mapView?.onLowMemory()
    }

    fun getMapAsync(callback: OnMapReadyCallback) {
        val mapView = this.mapView
        if (mapView == null) {
            this.callbacks.add(callback)
        } else {
            mapView.getMapAsync(callback)
        }
    }

    fun getMapAsync(callback: (NaverMap) -> Unit) {
        val mapView = this.mapView
        if (mapView == null) {
            this.callbacks.add(OnMapReadyCallback(callback))
        } else {
            mapView.getMapAsync(callback)
        }
    }
}
