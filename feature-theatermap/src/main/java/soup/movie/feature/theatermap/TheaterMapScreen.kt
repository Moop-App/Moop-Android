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
package soup.movie.feature.theatermap

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.NaverMapConstants
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.compose.rememberMarkerState
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.launch
import soup.movie.core.designsystem.icon.MovieIcons
import soup.movie.core.external.Cgv
import soup.movie.core.external.LotteCinema
import soup.movie.core.external.Megabox
import soup.movie.feature.theatermap.internal.CgvMarkerUiModel
import soup.movie.feature.theatermap.internal.LauncherIcons
import soup.movie.feature.theatermap.internal.LotteCinemaMarkerUiModel
import soup.movie.feature.theatermap.internal.MegaboxMarkerUiModel
import soup.movie.feature.theatermap.internal.TheaterMarkerUiModel
import soup.movie.resources.R
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TheaterMapScreen(
    viewModel: TheaterMapViewModel,
) {
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val bottomSheetState = bottomSheetScaffoldState.bottomSheetState
    val bottomSheetVisible by remember {
        derivedStateOf {
            viewModel.selectedTheater != null
        }
    }
    LaunchedEffect(bottomSheetVisible) {
        coroutineScope.launch {
            if (bottomSheetVisible) {
                bottomSheetState.expand()
            } else {
                bottomSheetState.collapse()
            }
        }
    }
    BackHandler(enabled = bottomSheetState.isExpanded) {
        viewModel.onTheaterUnselected()
    }
    BottomSheetScaffold(
        modifier = Modifier.systemBarsPadding(),
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetElevation = if (MaterialTheme.colors.isLight) 16.dp else 0.dp,
        sheetContent = {
            TheaterMapFooter(
                selectedTheater = viewModel.selectedTheater,
                onClick = { viewModel.onTheaterUnselected() }
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.theater_map_title))
                }
            )
        },
    ) { paddingValues ->
        TheaterMapContents(
            modifier = Modifier.padding(paddingValues),
            theaters = viewModel.uiModel,
            selectedTheater = viewModel.selectedTheater,
            onTheaterClick = { viewModel.onTheaterSelected(it) },
            onMapClick = { viewModel.onTheaterUnselected() },
            onMapLoaded = { viewModel.onRefresh() }
        )
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun TheaterMapContents(
    theaters: List<TheaterMarkerUiModel>,
    selectedTheater: TheaterMarkerUiModel?,
    onTheaterClick: (TheaterMarkerUiModel) -> Unit,
    modifier: Modifier = Modifier,
    isLightTheme: Boolean = MaterialTheme.colors.isLight,
    onMapClick: () -> Unit = {},
    onMapLoaded: () -> Unit = {},
) {
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        this.position = CameraPosition(LatLng.INVALID, 12.0)
    }
    NaverMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            mapType = if (isLightTheme) {
                MapType.Basic
            } else {
                MapType.Navi
            },
            extent = LatLngBounds.from(
                LatLng(31.43, 122.37),
                LatLng(44.35, 132.0)
            ),
            minZoom = 6.0,
            isNightModeEnabled = isLightTheme.not(),
            backgroundColor = if (isLightTheme) {
                NaverMapConstants.DefaultBackgroundColorLight
            } else {
                NaverMapConstants.DefaultBackgroundColorDark
            },
            backgroundResource = if (isLightTheme) {
                NaverMapConstants.DefaultBackgroundDrawableLight
            } else {
                NaverMapConstants.DefaultBackgroundDrawableDark
            },
            locationTrackingMode = LocationTrackingMode.Follow,
        ),
        uiSettings = MapUiSettings(
            isScaleBarEnabled = false,
        ),
        locationSource = rememberFusedLocationSource(),
        onMapClick = { _, _ ->
            onMapClick()
        },
        onMapLoaded = { onMapLoaded() }
    ) {
        theaters.forEach { theater ->
            Marker(
                state = rememberMarkerState(
                    position = LatLng(theater.lat, theater.lng)
                ),
                captionText = theater.name,
                icon = theater.getMarkerIcon(),
                isHideCollidedSymbols = true,
                isHideCollidedCaptions = true,
                onClick = {
                    onTheaterClick(theater)
                    true
                }
            )
        }
    }

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(selectedTheater) {
        coroutineScope.launch {
            if (selectedTheater != null) {
                cameraPositionState.animate(
                    update = CameraUpdate.scrollAndZoomTo(
                        LatLng(selectedTheater.lat, selectedTheater.lng),
                        max(cameraPositionState.position.zoom, 16.0)
                    ),
                    animation = CameraAnimation.Fly,
                )
            } else {
                cameraPositionState.animate(
                    update = CameraUpdate.zoomTo(
                        min(cameraPositionState.position.zoom, 12.0)
                    ),
                    animation = CameraAnimation.Easing
                )
            }
        }
    }
}

@Composable
private fun TheaterMapFooter(
    selectedTheater: TheaterMarkerUiModel?,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .requiredHeight(100.dp)
            .clickable { onClick() },
    ) {
        val context = LocalContext.current
        val launcherIcons = remember(context) {
            LauncherIcons(context)
        }
        if (selectedTheater != null) {
            Text(
                text = selectedTheater.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 8.dp),
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
                        Uri.parse("nmap://place?lat=$lat&lng=$lng&name=${Uri.encode(name)}&appname=soup.movie")
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
            InfoButton(
                onClick = {
                    selectedTheater.executeWeb(context)
                }
            )
        }
    }
}

@Composable
private fun InfoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .padding(8.dp)
            .requiredSize(48.dp)
    ) {
        Icon(
            MovieIcons.Info,
            contentDescription = null
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

private fun TheaterMarkerUiModel.getMarkerIcon(): OverlayImage {
    return when (this) {
        is CgvMarkerUiModel -> TheaterMarkerIcons.Cgv
        is LotteCinemaMarkerUiModel -> TheaterMarkerIcons.LotteCinema
        is MegaboxMarkerUiModel -> TheaterMarkerIcons.Megabox
    }
}

private object TheaterMarkerIcons {
    val Cgv = OverlayImage.fromResource(MovieIcons.MarkerCgv)
    val LotteCinema = OverlayImage.fromResource(MovieIcons.MarkerLotte)
    val Megabox = OverlayImage.fromResource(MovieIcons.MarkerMegabox)
}

private fun TheaterMarkerUiModel.executeWeb(ctx: Context) {
    return when (this) {
        is CgvMarkerUiModel ->
            Cgv.executeWeb(ctx, code)
        is LotteCinemaMarkerUiModel ->
            LotteCinema.executeWeb(ctx, code)
        is MegaboxMarkerUiModel ->
            Megabox.executeWeb(ctx, code)
    }
}
