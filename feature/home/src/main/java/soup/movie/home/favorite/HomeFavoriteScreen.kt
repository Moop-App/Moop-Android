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
package soup.movie.home.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import soup.movie.ext.showToast
import soup.movie.home.tab.MovieList
import soup.movie.home.tab.NoMovieItems
import soup.movie.model.Movie

@Composable
internal fun HomeFavoriteScreen(
    movies: List<Movie>,
    onItemClick: (Movie) -> Unit
) {
    ProvideWindowInsets {
        Box(modifier = Modifier.fillMaxSize()) {
            if (movies.isEmpty()) {
                NoMovieItems(modifier = Modifier.align(Alignment.Center))
            } else {
                // TODO: scrollToTop 구현 필요
                //  - 선택된 Page에서 Back 버튼이 눌려졌을 때
                //  - AppBar에서 선택된 탭을 다시 선택했을 때
//                val coroutineScope = rememberCoroutineScope()
//                val listState = rememberLazyListState()
//                BackHandler(enabled = listState.isTop().not()) {
//                    coroutineScope.launch {
//                        listState.firstVisibleItemIndex
//                        if (listState.isScrollInProgress) {
//                            listState.stopScroll()
//                            listState.scrollToItem(0)
//                        } else {
//                            listState.animateScrollToItem(0)
//                        }
//                    }
//                }

                val context = LocalContext.current
                MovieList(
                    movies,
                    onItemClick = onItemClick,
                    onLongItemClick = {
                        context.showToast(it.title)
                    },
                    modifier = Modifier.navigationBarsPadding(start = false, end = false)
                )
            }
        }
    }
}

private fun LazyListState.isTop(): Boolean {
    return firstVisibleItemIndex == 0 && firstVisibleItemScrollOffset == 0
}