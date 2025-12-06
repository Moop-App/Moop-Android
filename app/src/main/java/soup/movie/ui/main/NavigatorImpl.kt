/*
 * Copyright 2025 SOUP
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
package soup.movie.ui.main

import androidx.navigation3.runtime.NavKey
import dagger.hilt.android.scopes.ActivityRetainedScoped
import soup.movie.feature.navigator.Navigator
import javax.inject.Inject

@ActivityRetainedScoped
class NavigatorImpl @Inject constructor(
    val state: NavigationState,
) : Navigator {

    override fun navigate(route: NavKey) {
        if (route in state.backStacks.keys) {
            if (route == state.topLevelRoute) {
                state.backStacks[route]?.let {
                    it.removeRange(1, it.size)
                }
            } else {
                state.topLevelRoute = route
            }
        } else {
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    override fun goBack() {
        val currentStack = state.backStacks[state.topLevelRoute] ?: error("Stack for ${state.topLevelRoute} not found")
        val currentRoute = currentStack.last()

        if (currentRoute == state.topLevelRoute) {
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeLastOrNull()
        }
    }
}
