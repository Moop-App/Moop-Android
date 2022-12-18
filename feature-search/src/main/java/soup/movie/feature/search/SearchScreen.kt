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
package soup.movie.feature.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import soup.movie.core.designsystem.showToast
import soup.movie.feature.home.tab.MovieList
import soup.movie.feature.home.tab.NoMovieItems
import soup.movie.model.Movie
import soup.movie.resources.R

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    upPress: () -> Unit,
    onItemClick: (Movie) -> Unit
) {
    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth().height(56.dp),
                color = MaterialTheme.colors.primarySurface,
                elevation = AppBarDefaults.TopAppBarElevation
            ) {
                val focusManager = LocalFocusManager.current
                val handleColor = MaterialTheme.colors.secondary
                val contentAlpha = ContentAlpha.medium
                val customTextSelectionColors = remember(handleColor, contentAlpha) {
                    TextSelectionColors(
                        handleColor = handleColor,
                        backgroundColor = handleColor.copy(alpha = contentAlpha)
                    )
                }
                CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                    val focusRequester = FocusRequester()
                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }

                    val query by viewModel.query.collectAsState()
                    TextField(
                        value = query,
                        onValueChange = { viewModel.onQueryChanged(it) },
                        modifier = Modifier.fillMaxSize()
                            .focusRequester(focusRequester),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                focusManager.clearFocus()
                            }
                        ),
                        singleLine = true,
                        placeholder = {
                            Text(stringResource(R.string.search_hint))
                        },
                        leadingIcon = {
                            IconButton(onClick = upPress) {
                                Icon(
                                    Icons.Default.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = { viewModel.onQueryChanged("") }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = null
                                )
                            }
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = MaterialTheme.colors.secondary,
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            leadingIconColor = MaterialTheme.colors.onSurface,
                            trailingIconColor = MaterialTheme.colors.onSurface
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        val uiModel by viewModel.uiModel.collectAsState()
        when (uiModel) {
            is SearchUiModel.None -> {}
            is SearchUiModel.Success -> {
                val model = uiModel as SearchUiModel.Success
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    if (model.hasNoItem) {
                        NoMovieItems(modifier = Modifier.align(Alignment.Center))
                    } else {
                        val context = LocalContext.current
                        MovieList(
                            movies = model.movies,
                            onItemClick = {
                                viewModel.onMovieClick()
                                onItemClick(it)
                            },
                            onLongItemClick = {
                                context.showToast(it.title)
                            }
                        )
                    }
                }
            }
        }
    }
}
