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
package soup.movie.theatermap.internal

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal suspend inline fun MapView.awaitMap(): NaverMap {
    return suspendCoroutine { continuation ->
        getMapAsync {
            continuation.resume(it)
        }
    }
}

@Composable
internal fun rememberSavedInstanceState(): Bundle {
    return rememberSaveable { Bundle() }
}

@Composable
internal fun rememberMapViewWithLifecycle(
    options: NaverMapOptions,
    savedInstanceState: Bundle = rememberSavedInstanceState()
): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context, options)
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, mapView, savedInstanceState) {
        val lifecycleObserver = getMapLifecycleObserver(
            mapView,
            savedInstanceState.takeUnless { it.isEmpty }
        )
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            mapView.onSaveInstanceState(savedInstanceState)
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

private fun getMapLifecycleObserver(
    mapView: MapView,
    savedInstanceState: Bundle?
): LifecycleEventObserver {
    return LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> mapView.onCreate(savedInstanceState)
            Lifecycle.Event.ON_START -> mapView.onStart()
            Lifecycle.Event.ON_RESUME -> mapView.onResume()
            Lifecycle.Event.ON_PAUSE -> mapView.onPause()
            Lifecycle.Event.ON_STOP -> mapView.onStop()
            Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
            else -> throw IllegalStateException()
        }
    }
}
