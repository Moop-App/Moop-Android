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
package soup.movie.feature.search.impl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import soup.movie.core.designsystem.icon.MovieIcons
import soup.movie.core.designsystem.theme.MovieTheme
import soup.movie.feature.home.rememberHomeComposableFactory
import soup.movie.model.MovieModel
import soup.movie.resources.R

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    upPress: () -> Unit,
    onItemClick: (MovieModel) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val factory = rememberHomeComposableFactory()
    val query by viewModel.query.collectAsState()
    val uiModel by viewModel.uiModel.collectAsState()

    Scaffold(
        containerColor = MovieTheme.colorScheme.surfaceContainerLowest,
        topBar = {
            SearchTopBar(
                upPress = upPress,
                query = query,
                onQueryChanged = { viewModel.onQueryChanged(it) },
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            if (uiModel.hasNoItem) {
                factory.NoMovieItems(modifier = Modifier.align(Alignment.Center))
            } else {
                val focusManager = LocalFocusManager.current
                factory.MovieList(
                    movies = uiModel.movies,
                    onItemClick = {
                        focusManager.clearFocus()
                        onItemClick(it)
                    },
                    onLongItemClick = {
                        coroutineScope.launch {
                            focusManager.clearFocus()
                            snackbarHostState.showSnackbar(message = it.title)
                        }
                    },
                    modifier = Modifier,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTopBar(
    upPress: () -> Unit,
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
) {
    Surface(
        modifier = modifier.windowInsetsPadding(windowInsets)
            .fillMaxWidth()
            .height(56.dp),
        color = MovieTheme.colorScheme.surfaceContainerLowest,
    ) {
        val focusManager = LocalFocusManager.current
        val focusRequester = FocusRequester()
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        TextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search,
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                },
            ),
            singleLine = true,
            placeholder = {
                Text(stringResource(R.string.search_hint))
            },
            leadingIcon = {
                IconButton(onClick = upPress) {
                    Icon(
                        MovieIcons.ArrowBack,
                        contentDescription = null,
                    )
                }
            },
            trailingIcon = {
                IconButton(onClick = { onQueryChanged("") }) {
                    Icon(
                        MovieIcons.Close,
                        contentDescription = null,
                    )
                }
            },
            colors = TextFieldDefaults.colors(),
        )
    }
}

@PreviewLightDark
@Composable
private fun SearchTopBarPreview() {
    MovieTheme {
        SearchTopBar(
            upPress = {},
            query = "",
            onQueryChanged = {},
        )
    }
}
