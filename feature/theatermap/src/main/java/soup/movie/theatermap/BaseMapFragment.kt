/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soup.movie.theatermap

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback

abstract class BaseMapFragment : Fragment {

    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

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
        // this.mapView?.onDestroy()
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
