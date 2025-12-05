package soup.movie.feature.navigator

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope

typealias EntryProviderInstaller = EntryProviderScope<Screen>.() -> Unit

interface Navigator {
    val backStack: SnapshotStateList<Screen>
    fun navigate(destination: Screen)
    fun goBack()
}
