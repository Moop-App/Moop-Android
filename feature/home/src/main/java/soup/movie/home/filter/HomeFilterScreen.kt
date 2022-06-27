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
package soup.movie.home.filter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.flowlayout.FlowRow
import soup.metronome.material.chip.ChipDefaults
import soup.metronome.material.chip.FilterChip
import soup.movie.home.R
import soup.movie.ui.divider

@Composable
internal fun HomeFilterScreen(viewModel: HomeFilterViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                WindowInsets.navigationBars
                    .only(WindowInsetsSides.Top + WindowInsetsSides.Bottom)
                    .asPaddingValues()
            ),
    ) {
        HomeFilterTheater(viewModel)
        HomeFilterDivider()
        HomeFilterAge(viewModel)
        HomeFilterDivider()
        HomeFilterGenre(
            items = viewModel.genreFilterList,
            onCheckedChange = { genreFilter, isChecked ->
                viewModel.onGenreFilterClick(genreFilter.name, isChecked)
            }
        )
    }
}

@Composable
private fun HomeFilterDivider() {
    Divider(
        color = MaterialTheme.colors.divider,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
private fun HomeFilterCategory(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colors.onBackground,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun HomeFilterTheater(viewModel: HomeFilterViewModel) {
    val theaterUiModel by viewModel.theaterUiModel.observeAsState()
    Column(modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)) {
        HomeFilterCategory(text = stringResource(R.string.filter_category_theater))
        Row(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Center
        ) {
            theaterUiModel?.let { uiModel ->
                CgvFilterChip(
                    text = "CGV",
                    checked = uiModel.hasCgv,
                    onCheckedChange = { isChecked ->
                        viewModel.onCgvFilterChanged(isChecked)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                LotteFilterChip(
                    text = "롯데시네마",
                    checked = uiModel.hasLotteCinema,
                    onCheckedChange = { isChecked ->
                        viewModel.onLotteFilterChanged(isChecked)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                MegaboxFilterChip(
                    text = "메가박스",
                    checked = uiModel.hasMegabox,
                    onCheckedChange = { isChecked ->
                        viewModel.onMegaboxFilterChanged(isChecked)
                    }
                )
            }
        }
    }
}

@Composable
private fun HomeFilterAge(viewModel: HomeFilterViewModel) {
    val ageUiModel by viewModel.ageUiModel.observeAsState()
    Column(modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)) {
        HomeFilterCategory(text = stringResource(R.string.filter_category_age))
        Row(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Center
        ) {
            ageUiModel?.let {
                HomeFilterAgeText(
                    text = "전체",
                    selected = it.hasAll,
                    modifier = Modifier
                        .padding(end = 2.dp)
                        .clickable { viewModel.onAgeAllFilterClicked() }
                        .background(
                            color = if (it.hasAll) Color(0xFF4CAF50) else Color(0x664CAF50),
                            shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                        )
                )
                HomeFilterAgeText(
                    text = "12",
                    selected = it.has12,
                    modifier = Modifier
                        .padding(end = 2.dp)
                        .clickable { viewModel.onAge12FilterClicked() }
                        .background(
                            color = if (it.has12) Color(0xFF2196F3) else Color(0x662196F3)
                        )
                )
                HomeFilterAgeText(
                    text = "15",
                    selected = it.has15,
                    modifier = Modifier
                        .padding(end = 2.dp)
                        .clickable { viewModel.onAge15FilterClicked() }
                        .background(
                            color = if (it.has15) Color(0xFFFFC107) else Color(0x66FFC107)
                        )
                )
                HomeFilterAgeText(
                    text = "청불",
                    selected = it.has19,
                    modifier = Modifier
                        .clickable { viewModel.onAge19FilterClicked() }
                        .background(
                            color = if (it.has19) Color(0xFFF44336) else Color(0x66F44336),
                            shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                        )
                )
            }
        }
    }
}

@Composable
private fun HomeFilterAgeText(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    val textColor = if (selected) {
        Color.White
    } else {
        Color(0xAAFFFFFF)
    }
    Text(
        text = text,
        color = textColor,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = modifier
            .requiredWidth(72.dp)
            .wrapContentHeight()
            .padding(vertical = 6.dp)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HomeFilterGenre(
    items: List<GenreFilterItem>,
    onCheckedChange: (GenreFilterItem, Boolean) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)) {
        HomeFilterCategory(text = stringResource(R.string.filter_category_genre))
        FlowRow(
            modifier = Modifier.padding(top = 12.dp),
            mainAxisSpacing = 8.dp
        ) {
            items.forEach { genreFilter ->
                GenreFilterChip(
                    genreFilter.name,
                    checked = genreFilter.isChecked,
                    onCheckedChange = { onCheckedChange(genreFilter, it) }
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun GenreFilterChip(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    CompositionLocalProvider(LocalRippleTheme provides rippleTheme(Color(0xFFBDBDBD))) {
        FilterChip(
            checked = checked,
            onCheckedChange = onCheckedChange,
            chipIcon = {
                Image(
                    painterResource(R.drawable.ic_logo_genre),
                    contentDescription = null
                )
            },
            checkedIcon = {
                Image(
                    painterResource(R.drawable.ic_check_genre),
                    contentDescription = null
                )
            },
            enabled = enabled,
            colors = ChipDefaults.filterChipColors(
                checkedBackgroundColor = Color(0xEEDDDDDD),
                checkedContentColor = Color(0x88000000),
                uncheckedBackgroundColor = Color(0x33DDDDDD),
                uncheckedContentColor = Color(0x44000000)
            )
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(ChipDefaults.TextPadding)
            )
        }
    }
}

@Composable
internal fun rippleTheme(color: Color) = object : RippleTheme {

    @Composable
    override fun defaultColor(): Color {
        return color
    }

    @Composable
    override fun rippleAlpha(): RippleAlpha {
        return RippleAlpha(1f, 1f, 1f, 1f)
    }
}
