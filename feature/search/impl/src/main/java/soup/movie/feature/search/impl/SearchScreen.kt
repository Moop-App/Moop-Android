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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import soup.movie.core.designsystem.icon.MovieIcons
import soup.movie.core.designsystem.showToast
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
    val factory = rememberHomeComposableFactory()
    val query by viewModel.query.collectAsState()
    val uiModel by viewModel.uiModel.collectAsState()
    SearchScaffold(
        upPress = upPress,
        query = query,
        onQueryChanged = { viewModel.onQueryChanged(it) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            if (uiModel.hasNoItem) {
                factory.NoMovieItems(modifier = Modifier.align(Alignment.Center))
            } else {
                val context = LocalContext.current
                factory.MovieList(
                    movies = uiModel.movies,
                    onItemClick = {
                        onItemClick(it)
                    },
                    onLongItemClick = {
                        context.showToast(it.title)
                    },
                    modifier = Modifier,
                )
            }
        }
    }
}

@Composable
private fun SearchScaffold(
    upPress: () -> Unit,
    query: String,
    onQueryChanged: (String) -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                color = MovieTheme.colors.primary,
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
        },
    ) { paddingValues ->
        content(paddingValues)
    }
}

@PreviewLightDark
@Composable
private fun SearchScaffoldPreview() {
    MovieTheme {
        SearchScaffold(
            upPress = {},
            query = "",
            onQueryChanged = {},
        ) {}
    }
}
