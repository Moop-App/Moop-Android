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
package soup.movie.feature.settings.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator

/**
 * Create a navigation state for settings feature that persists config changes and process death.
 */
@Composable
fun rememberSettingsNavigationState(
    startRoute: NavKey,
): SettingsNavigationState {
    val backStack = rememberNavBackStack(startRoute)

    return remember(startRoute) {
        SettingsNavigationState(
            startRoute = startRoute,
            backStack = backStack,
        )
    }
}

/**
 * State holder for settings navigation state.
 */
class SettingsNavigationState(
    val startRoute: NavKey,
    val backStack: NavBackStack<NavKey>,
)

/**
 * Convert SettingsNavigationState into NavEntries.
 */
@Composable
fun SettingsNavigationState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>,
): SnapshotStateList<NavEntry<NavKey>> {
    val decorators = listOf(
        rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
    )

    val decoratedEntries = rememberDecoratedNavEntries(
        backStack = backStack,
        entryDecorators = decorators,
        entryProvider = entryProvider,
    )

    return decoratedEntries.toMutableStateList()
}
