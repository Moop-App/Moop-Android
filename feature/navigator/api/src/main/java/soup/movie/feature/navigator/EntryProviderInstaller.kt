package soup.movie.feature.navigator

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey

typealias EntryProviderInstaller = EntryProviderScope<NavKey>.() -> Unit

interface Navigator {
    fun navigate(route: NavKey)
    fun goBack()
}
