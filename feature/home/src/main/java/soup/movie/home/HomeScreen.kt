/*
 * Copyright 2022 SOUP
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
package soup.movie.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Scaffold
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Surface
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.primarySurface
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import soup.metronome.visibility.visible
import soup.movie.analytics.EventAnalytics
import soup.movie.ext.showToast
import soup.movie.home.favorite.HomeFavoriteList
import soup.movie.home.filter.HomeFilterScreen
import soup.movie.home.now.HomeNowList
import soup.movie.home.plan.HomePlanList
import soup.movie.model.Movie
import soup.movie.ui.divider

private enum class Page {
    Now,
    Plan,
    Favorite,
    ;

    companion object {
        fun of(page: Int): Page {
            return when (page) {
                0 -> Now
                1 -> Plan
                else -> Favorite
            }
        }
    }
}

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalPagerApi::class,
)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    analytics: EventAnalytics,
    onNavigationClick: () -> Unit,
    onTabSelected: (HomeTabUiModel) -> Unit,
    onMovieItemClick: (Movie) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val uiModel by viewModel.uiModel.observeAsState(HomeTabUiModel.Now)
    val pages = Page.values()
    val gridStates = pages.map { rememberLazyGridState() }
    val pagerState = rememberPagerState()
    LaunchedEffect(uiModel) {
        val page = when (uiModel) {
            HomeTabUiModel.Now -> 0
            HomeTabUiModel.Plan -> 1
            HomeTabUiModel.Favorite -> 2
        }
        pagerState.scrollToPage(page)
    }
    val currentPage by remember {
        derivedStateOf {
            pagerState.currentPage
        }
    }
    val isScrollInProgress = pagerState.isScrollInProgress
    LaunchedEffect(currentPage, isScrollInProgress) {
        if (isScrollInProgress.not()) {
            val tab = when (Page.of(currentPage)) {
                Page.Now -> HomeTabUiModel.Now
                Page.Plan -> HomeTabUiModel.Plan
                Page.Favorite -> HomeTabUiModel.Favorite
            }
            onTabSelected(tab)
        }
    }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )
    val bottomSheetState = bottomSheetScaffoldState.bottomSheetState
    val bottomSheetVisible by remember {
        derivedStateOf {
            bottomSheetState.isExpanded
        }
    }
    val isTopAtCurrentTab by remember {
        derivedStateOf {
            gridStates[currentPage].let {
                it.firstVisibleItemIndex == 0 && it.firstVisibleItemScrollOffset == 0
            }
        }
    }
    BackHandler(enabled = bottomSheetVisible || isTopAtCurrentTab.not() || currentPage > 0) {
        if (bottomSheetVisible) {
            coroutineScope.launch {
                bottomSheetState.collapse()
            }
        }
        if (isTopAtCurrentTab.not()) {
            val currentGridState = gridStates[currentPage]
            coroutineScope.launch {
                currentGridState.animateScrollToItem(0)
            }
        } else {
            coroutineScope.launch {
                pagerState.animateScrollToPage(0)
            }
        }
    }
    BottomSheetScaffold(
        modifier = Modifier.statusBarsPadding(),
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetElevation = if (MaterialTheme.colors.isLight) 16.dp else 0.dp,
        sheetContent = {
            HomeFilterScreen()
        }
    ) {
        Scaffold(
            topBar = {
                Column {
                    CustomAppBar(
                        navigationIcon = {
                            IconButton(onClick = { onNavigationClick() }) {
                                Icon(
                                    Icons.Outlined.Menu,
                                    contentDescription = null
                                )
                            }
                        },
                        title = {
                            Row {
                                val scrollState = rememberScrollState()
                                val showDivider by remember {
                                    derivedStateOf {
                                        scrollState.value != 0
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .visible(showDivider)
                                        .padding(vertical = 4.dp)
                                        .width(1.dp)
                                        .fillMaxHeight()
                                        .background(color = MaterialTheme.colors.divider)
                                )
                                ScrollableTabRow(
                                    selectedTabIndex = currentPage,
                                    edgePadding = 0.dp,
                                    indicator = { tabPositions ->
                                        Box(
                                            modifier = Modifier
                                                .tabIndicatorOffset(tabPositions[currentPage])
                                                .fillMaxSize()
                                                .requiredHeight(36.dp)
                                                .background(
                                                    color = MaterialTheme.colors.secondary.copy(
                                                        alpha = 0.15f
                                                    ),
                                                    shape = CircleShape
                                                )
                                        )
                                    },
                                    divider = {},
                                ) {
                                    pages.forEachIndexed { index, page ->
                                        when (page) {
                                            Page.Now -> HomeNowTab(
                                                isSelected = currentPage == index,
                                                onTabSelected = {
                                                    coroutineScope.launch {
                                                        pagerState.animateScrollToPage(index)
                                                    }
                                                },
                                                onTabReselected = {
                                                    coroutineScope.launch {
                                                        gridStates[index].animateScrollToItem(0)
                                                    }
                                                }
                                            )
                                            Page.Plan -> HomePlanTab(
                                                isSelected = currentPage == index,
                                                onTabSelected = {
                                                    coroutineScope.launch {
                                                        pagerState.animateScrollToPage(index)
                                                    }
                                                },
                                                onTabReselected = {
                                                    coroutineScope.launch {
                                                        gridStates[index].animateScrollToItem(0)
                                                    }
                                                }
                                            )
                                            Page.Favorite -> HomeFavoriteTab(
                                                isSelected = currentPage == index,
                                                onTabSelected = {
                                                    coroutineScope.launch {
                                                        pagerState.animateScrollToPage(index)
                                                    }
                                                },
                                                onTabReselected = {
                                                    coroutineScope.launch {
                                                        gridStates[index].animateScrollToItem(0)
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            },
            floatingActionButton = {
                HomeFilterButton(
                    onClick = {
                        analytics.clickMenuFilter()
                        coroutineScope.launch {
                            bottomSheetState.expand()
                        }
                    },
                    modifier = Modifier.padding(
                        WindowInsets.navigationBars
                            .only(WindowInsetsSides.Bottom)
                            .asPaddingValues()
                    ),
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                val context = LocalContext.current
                HorizontalPager(count = pages.size, state = pagerState) { page ->
                    when (Page.of(page)) {
                        Page.Now -> HomeNowList(
                            state = gridStates[page],
                            onItemClick = onMovieItemClick,
                            onItemLongClick = {
                                context.showToast(it.title)
                            }
                        )
                        Page.Plan -> HomePlanList(
                            state = gridStates[page],
                            onItemClick = onMovieItemClick,
                            onItemLongClick = {
                                context.showToast(it.title)
                            }
                        )
                        Page.Favorite -> HomeFavoriteList(
                            state = gridStates[page],
                            onItemClick = onMovieItemClick,
                            onItemLongClick = {
                                context.showToast(it.title)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = AppBarDefaults.TopAppBarElevation
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        elevation = elevation,
        shape = RectangleShape,
        modifier = modifier
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
            Row(
                modifier = Modifier.fillMaxWidth().height(56.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (navigationIcon != null) {
                    Row(
                        modifier = Modifier.fillMaxHeight().requiredWidth(56.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        navigationIcon()
                    }
                }
                Row(
                    modifier = Modifier.fillMaxHeight().weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ProvideTextStyle(
                        value = MaterialTheme.typography.h6,
                        content = title,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun HomeNowTab(
    isSelected: Boolean,
    onTabSelected: () -> Unit,
    onTabReselected: () -> Unit,
) {
    HomeTab(
        AnimatedImageVector.animatedVectorResource(R.drawable.avd_home_now_selected),
        label = stringResource(R.string.menu_now),
        isSelected = isSelected,
        onTabSelected = onTabSelected,
        onTabReselected = onTabReselected,
    )
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun HomePlanTab(
    isSelected: Boolean,
    onTabSelected: () -> Unit,
    onTabReselected: () -> Unit,
) {
    HomeTab(
        AnimatedImageVector.animatedVectorResource(R.drawable.avd_home_plan_selected),
        label = stringResource(R.string.menu_plan),
        isSelected = isSelected,
        onTabSelected = onTabSelected,
        onTabReselected = onTabReselected,
    )
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun HomeFavoriteTab(
    isSelected: Boolean,
    onTabSelected: () -> Unit,
    onTabReselected: () -> Unit,
) {
    HomeTab(
        AnimatedImageVector.animatedVectorResource(R.drawable.avd_favorite_selected),
        label = stringResource(R.string.menu_favorite),
        isSelected = isSelected,
        onTabSelected = onTabSelected,
        onTabReselected = onTabReselected,
    )
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun HomeTab(
    imageVector: AnimatedImageVector,
    label: String,
    isSelected: Boolean,
    onTabSelected: () -> Unit,
    onTabReselected: () -> Unit,
) {
    LeadingIconTab(
        modifier = Modifier
            .fillMaxHeight()
            .requiredHeight(36.dp)
            .clip(shape = RoundedCornerShape(percent = 50)),
        selected = isSelected,
        onClick = {
            if (isSelected) {
                onTabReselected()
            } else {
                onTabSelected()
            }
        },
        icon = {
            Icon(
                rememberAnimatedVectorPainter(imageVector, isSelected),
                contentDescription = null,
                tint = if (isSelected) {
                    MaterialTheme.colors.secondary
                } else {
                    MaterialTheme.colors.onSurface
                },
            )
        },
        text = {
            Text(
                text = label,
            )
        },
        selectedContentColor = MaterialTheme.colors.secondary,
        unselectedContentColor = MaterialTheme.colors.onSurface,
    )
}

@Composable
private fun LeadingIconTab(
    selected: Boolean,
    onClick: () -> Unit,
    text: @Composable (() -> Unit),
    icon: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    selectedContentColor: Color = LocalContentColor.current,
    unselectedContentColor: Color = selectedContentColor.copy(alpha = ContentAlpha.medium)
) {
    val color = if (selected) selectedContentColor else unselectedContentColor
    CompositionLocalProvider(
        LocalContentColor provides color.copy(alpha = 1f),
        LocalContentAlpha provides color.alpha,
    ) {
        Row(
            modifier = modifier
                .height(48.dp)
                .selectable(
                    selected = selected,
                    onClick = onClick,
                    enabled = enabled,
                    role = Role.Tab,
                    interactionSource = interactionSource,
                    indication = null
                )
                .padding(horizontal = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            Spacer(Modifier.requiredWidth(8.dp))
            val style = MaterialTheme.typography.button.copy(textAlign = TextAlign.Center)
            ProvideTextStyle(style, content = text)
        }
    }
}

@Composable
private fun HomeFilterButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            Icons.Rounded.FilterList,
            contentDescription = stringResource(R.string.menu_filter)
        )
    }
}
