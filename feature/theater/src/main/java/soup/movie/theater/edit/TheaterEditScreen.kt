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
package soup.movie.theater.edit

import android.view.MotionEvent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import soup.movie.model.Theater
import soup.movie.theater.R
import soup.movie.theater.TheaterChip
import soup.movie.theater.divider
import soup.movie.util.debounce

private enum class Page(val title: String) {
    CGV("CGV"),
    Lotte("롯데시네마"),
    Megabox("메가박스");

    companion object {
        fun of(page: Int): Page {
            return when (page) {
                0 -> CGV
                1 -> Lotte
                else -> Megabox
            }
        }
    }
}

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalPagerApi::class
)
@Composable
internal fun TheaterEditScreen(viewModel: TheaterEditViewModel, upPress: () -> Unit) {
    ProvideWindowInsets {
        val pages = Page.values()
        val pagerState = rememberPagerState(
            pageCount = pages.size,
            initialOffscreenLimit = pages.size
        )
        val coroutineScope = rememberCoroutineScope()
        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
        FirstLaunchedEffect {
            delay(500)
            bottomSheetScaffoldState.bottomSheetState.expand()
            delay(500)
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }
        BottomSheetScaffold(
            scaffoldState = bottomSheetScaffoldState,
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding(start = false, end = false),
            topBar = {
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                            color = MaterialTheme.colors.secondary
                        )
                    },
                    modifier = Modifier.shadow(elevation = 4.dp)
                ) {
                    pages.forEachIndexed { index, page ->
                        Tab(
                            text = { Text(page.title) },
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            selectedContentColor = MaterialTheme.colors.secondary,
                            unselectedContentColor = MaterialTheme.colors.onBackground
                        )
                    }
                }
            },
            sheetPeekHeight = 60.dp,
            sheetElevation = if (MaterialTheme.colors.isLight) 16.dp else 0.dp,
            sheetContent = {
                val uiModel by viewModel.footerUiModel.collectAsState(
                    TheaterEditFooterUiModel(emptyList())
                )
                TheaterEditFooter(
                    uiModel = uiModel,
                    onClick = {
                        coroutineScope.launch {
                            bottomSheetScaffoldState.bottomSheetState.toggle()
                        }
                    },
                    onTheaterClick = {
                        viewModel.remove(it)
                    },
                    onConfirmClick = {
                        viewModel.onConfirmClick()
                        upPress()
                    }
                )
            },
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .pointerInteropFilter {
                        if (it.action == MotionEvent.ACTION_DOWN) {
                            if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                                coroutineScope.launch {
                                    bottomSheetScaffoldState.bottomSheetState.collapse()
                                }
                                return@pointerInteropFilter true
                            }
                        }
                        false
                    }
            ) {
                HorizontalPager(state = pagerState) { page ->
                    when (Page.of(page)) {
                        Page.CGV -> CgvScreen(viewModel)
                        Page.Lotte -> LotteScreen(viewModel)
                        Page.Megabox -> MegaboxScreen(viewModel)
                    }
                }
                val viewState by viewModel.contentUiModel.collectAsState(
                    TheaterEditContentUiModel.LoadingState
                )
                if (viewState is TheaterEditContentUiModel.LoadingState) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.secondary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun TheaterEditFooter(
    uiModel: TheaterEditFooterUiModel,
    onClick: () -> Unit,
    onTheaterClick: (Theater) -> Unit,
    onConfirmClick: () -> Unit,
) {
    Column(
        modifier = Modifier.clickable(
            onClick = { debounce(onClick) },
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        )
    ) {
        TheaterEditFooterPeek(
            currentCount = uiModel.theaterList.size,
            isFull = uiModel.isFull(),
            onConfirmClick = onConfirmClick
        )
        TheaterEditFooterContents(
            theaterList = uiModel.theaterList,
            onTheaterClick = onTheaterClick
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TheaterEditFooterPeek(
    currentCount: Int,
    isFull: Boolean,
    onConfirmClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(60.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "자주가는 극장은 최대 10개까지\n선택할 수 있습니다.",
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f),
            fontSize = 14.sp,
            color = MaterialTheme.colors.onSurface
        )
        Surface(
            onClick = { debounce(onConfirmClick) },
            modifier = Modifier
                .padding(end = 8.dp)
                .requiredSize(100.dp, 36.dp),
            shape = RoundedCornerShape(percent = 50),
            color = if (isFull) MaterialTheme.colors.error else MaterialTheme.colors.secondary
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painterResource(R.drawable.ic_round_check),
                    contentDescription = null,
                    modifier = Modifier.padding(start = 8.dp)
                )
                // TODO: Ticker Animation
                Text(
                    text = currentCount.toString(),
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .weight(1f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                )
                Text(
                    text = "/ 10",
                    modifier = Modifier.padding(end = 16.dp),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun TheaterEditFooterContents(
    theaterList: List<Theater>,
    onTheaterClick: (Theater) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(180.dp)
    ) {
        Divider(color = MaterialTheme.colors.divider)
        if (theaterList.isEmpty()) {
            Text(
                text = stringResource(R.string.theater_empty_description),
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2
            )
        } else {
            FlowRow(
                modifier = Modifier.padding(all = 16.dp),
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 8.dp
            ) {
                theaterList.forEach { theater ->
                    TheaterChip(theater, onTheaterClick)
                }
            }
        }
    }
}

@Composable
private fun FirstLaunchedEffect(block: suspend CoroutineScope.() -> Unit) {
    var first by rememberSaveable { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        if (first) {
            first = false
            block()
        }
    }
}

@ExperimentalMaterialApi
private suspend fun BottomSheetState.toggle() {
    if (isCollapsed) {
        expand()
    } else {
        collapse()
    }
}
