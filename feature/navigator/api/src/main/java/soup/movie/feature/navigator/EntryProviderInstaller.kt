package soup.movie.feature.navigator

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey

typealias EntryProviderInstaller = EntryProviderScope<NavKey>.() -> Unit

interface Navigator {
    val backStack: SnapshotStateList<NavKey>
    fun navigate(destination: NavKey)
    fun goBack()
}
