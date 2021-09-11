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

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationSource
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.launch
import soup.movie.BuildConfig
import soup.movie.model.Theater
import soup.movie.system.SystemViewModel
import soup.movie.theatermap.R
import soup.movie.util.Cgv
import soup.movie.util.LauncherIcons
import soup.movie.util.LotteCinema
import soup.movie.util.Megabox
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun TheaterMapScreen(
    viewModel: TheaterMapViewModel,
    systemViewModel: SystemViewModel,
    locationSource: LocationSource?
) {
    ProvideWindowInsets {
        Scaffold(
            topBar = {
                Toolbar(
                    text = stringResource(R.string.theater_map_title),
                    onNavigationOnClick = { systemViewModel.openNavigationMenu() }
                )
            },
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding(start = false, end = false)
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                val coroutineScope = rememberCoroutineScope()
                val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
                var coverVisible by remember { mutableStateOf(true) }
                val bottomSheetVisible = viewModel.selectedTheater != null
                LaunchedEffect(bottomSheetVisible) {
                    coroutineScope.launch {
                        if (bottomSheetVisible) {
                            sheetState.show()
                        } else {
                            sheetState.hide()
                        }
                    }
                }
                LaunchedEffect(sheetState.isVisible) {
                    if (sheetState.isVisible.not()) {
                        viewModel.onTheaterUnselected()
                    }
                }
                BackHandler(enabled = sheetState.isVisible) {
                    viewModel.onTheaterUnselected()
                }
                val selectedTheater = viewModel.selectedTheater
                ModalBottomSheetLayout(
                    sheetState = sheetState,
                    sheetElevation = 16.dp,
                    scrimColor = Color.Transparent,
                    sheetContent = {
                        TheaterMapFooter(
                            selectedTheater = selectedTheater,
                            onClick = { viewModel.onTheaterUnselected() }
                        )
                    }
                ) {
                    val uiModel = viewModel.uiModel.observeAsState(emptyList())
                    TheaterMapContents(
                        theaters = uiModel.value,
                        selectedTheater = selectedTheater,
                        onTheaterClick = { viewModel.onTheaterSelected(it) },
                        modifier = Modifier.padding(paddingValues),
                        locationSource = locationSource,
                        onMapClick = { viewModel.onTheaterUnselected() },
                        onMapUpdated = {
                            coverVisible = false
                            viewModel.onRefresh()
                        }
                    )
                }
                TheaterMapCover(visible = coverVisible)
            }
        }
    }
}

@Composable
private fun TheaterMapContents(
    theaters: List<TheaterMarkerUiModel>,
    selectedTheater: TheaterMarkerUiModel?,
    onTheaterClick: (TheaterMarkerUiModel) -> Unit,
    modifier: Modifier = Modifier,
    locationSource: LocationSource? = null,
    locationTrackingMode: LocationTrackingMode = LocationTrackingMode.Follow,
    isLightTheme: Boolean = MaterialTheme.colors.isLight,
    onMapClick: () -> Unit = {},
    onMapUpdated: () -> Unit = {},
) {
    var markers by remember {
        mutableStateOf<List<Marker>>(emptyList())
    }
    val coroutineScope = rememberCoroutineScope()
    val mapView = rememberMapViewWithLifecycle()
    AndroidView(
        factory = {
            coroutineScope.launch {
                val naverMap = mapView.awaitMap()
                if (isLightTheme) {
                    naverMap.mapType = NaverMap.MapType.Basic
                    naverMap.isNightModeEnabled = false
                } else {
                    naverMap.mapType = NaverMap.MapType.Navi
                    naverMap.isNightModeEnabled = true
                }
                naverMap.locationSource = locationSource
                naverMap.locationTrackingMode = locationTrackingMode
                naverMap.moveCamera(CameraUpdate.zoomTo(12.0))
                naverMap.setOnMapClickListener { _, _ -> onMapClick() }
                onMapUpdated()
            }
            mapView
        },
        modifier = modifier
    )
    LaunchedEffect(theaters) {
        coroutineScope.launch {
            mapView.awaitMap().let { naverMap ->
                markers.forEach {
                    it.map = null
                }
                markers = theaters.map {
                    createMarker(it, onTheaterClick).apply {
                        map = naverMap
                    }
                }
            }
        }
    }
    LaunchedEffect(selectedTheater) {
        coroutineScope.launch {
            val naverMap = mapView.awaitMap()
            if (selectedTheater != null) {
                naverMap.moveCamera(
                    CameraUpdate
                        .scrollAndZoomTo(
                            LatLng(selectedTheater.lat, selectedTheater.lng),
                            max(naverMap.cameraPosition.zoom, 16.0)
                        )
                        .animate(CameraAnimation.Fly)
                )
            } else {
                naverMap.moveCamera(
                    CameraUpdate
                        .zoomTo(min(naverMap.cameraPosition.zoom, 12.0))
                        .animate(CameraAnimation.Easing)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TheaterMapFooter(
    selectedTheater: TheaterMarkerUiModel?,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .requiredHeight(100.dp)
            .background(color = MaterialTheme.colors.surface)
            .clickable { onClick() },
    ) {
        val context = LocalContext.current
        val launcherIcons = rememberLauncherIcons()
        if (selectedTheater != null) {
            Text(
                text = selectedTheater.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 8.dp),
                color = MaterialTheme.colors.onSurface,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
            )
            val googleMapPackage = "com.google.android.apps.maps"
            MapButton(
                appIcon = remember(context, googleMapPackage) {
                    launcherIcons.getAppIcon(context, googleMapPackage)
                },
                onClick = {
                    val gmmIntentUri = selectedTheater.run {
                        Uri.parse("geo:$lat,$lng?q=${Uri.encode(name)}")
                    }
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage(googleMapPackage)
                    context.startActivity(mapIntent)
                }
            )
            val naverMapPackage = "com.nhn.android.nmap"
            MapButton(
                appIcon = remember(context, naverMapPackage) {
                    launcherIcons.getAppIcon(context, naverMapPackage)
                },
                onClick = {
                    val gmmIntentUri = selectedTheater.run {
                        Uri.parse("nmap://place?lat=$lat&lng=$lng&name=${Uri.encode(name)}&appname=${BuildConfig.APPLICATION_ID}")
                    }
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage(naverMapPackage)
                    context.startActivity(mapIntent)
                }
            )
            val kakaoMapPackage = "net.daum.android.map"
            MapButton(
                appIcon = remember(context, kakaoMapPackage) {
                    launcherIcons.getAppIcon(context, kakaoMapPackage)
                },
                onClick = {
                    val gmmIntentUri = selectedTheater.run {
                        Uri.parse("geo:$lat,$lng?q=${Uri.encode(name)}")
                    }
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage(kakaoMapPackage)
                    context.startActivity(mapIntent)
                }
            )
            InfoButton(selectedTheater)
        }
    }
}

@Composable
private fun InfoButton(
    selectedTheater: TheaterMarkerUiModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    IconButton(
        onClick = {
            selectedTheater.executeWeb(context)
        },
        modifier = modifier
            .padding(8.dp)
            .requiredSize(48.dp)
    ) {
        Icon(
            Icons.Rounded.Info,
            contentDescription = null,
            tint = MaterialTheme.colors.onSurface,
        )
    }
}

@Composable
private fun MapButton(
    appIcon: Drawable?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (appIcon != null) {
        IconButton(
            onClick = onClick,
            modifier = modifier
                .padding(6.dp)
                .requiredSize(48.dp)
                .shadow(elevation = 12.dp, CircleShape)
        ) {
            Image(
                rememberDrawablePainter(appIcon),
                contentDescription = null,
                contentScale = ContentScale.Inside,
            )
        }
    }
}

@Composable
private fun rememberLauncherIcons(): LauncherIcons {
    val context = LocalContext.current
    return remember(context) { LauncherIcons(context) }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun TheaterMapCover(
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier.fillMaxSize(),
        enter = EnterTransition.None,
        exit = fadeOut(animationSpec = tween(delayMillis = 500))
    ) {
        val isLightTheme = MaterialTheme.colors.isLight
        val color = if (isLightTheme) {
            Color(NaverMap.DEFAULT_BACKGROUND_COLOR_LIGHT)
        } else {
            Color(NaverMap.DEFAULT_BACKGROUND_COLOR_DARK)
        }
        Spacer(modifier = Modifier.background(color))
    }
}

@Composable
private fun Toolbar(text: String, onNavigationOnClick: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onNavigationOnClick) {
                Icon(
                    Icons.Outlined.Menu,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onBackground,
                )
            }
        },
        title = {
            Text(
                text = text,
                color = MaterialTheme.colors.onBackground,
            )
        }
    )
}

private fun createMarker(
    theater: TheaterMarkerUiModel,
    onTheaterClick: (TheaterMarkerUiModel) -> Unit
) = Marker().apply {
    captionText = theater.name
    position = LatLng(theater.lat, theater.lng)
    icon = OverlayImage.fromResource(theater.getMarkerIcon())
    isHideCollidedSymbols = true
    isHideCollidedCaptions = true
    setOnClickListener {
        onTheaterClick(theater)
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
            Cgv.executeWeb(ctx, Theater(Theater.TYPE_CGV, code, name, lng, lat))
        is LotteCinemaMarkerUiModel ->
            LotteCinema.executeWeb(ctx, Theater(Theater.TYPE_LOTTE, code, name, lng, lat))
        is MegaboxMarkerUiModel ->
            Megabox.executeWeb(ctx, Theater(Theater.TYPE_MEGABOX, code, name, lng, lat))
    }
}
