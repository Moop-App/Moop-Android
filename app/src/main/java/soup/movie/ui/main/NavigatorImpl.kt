package soup.movie.ui.main

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import dagger.hilt.android.scopes.ActivityRetainedScoped
import soup.movie.feature.home.HomeScreenKey
import soup.movie.feature.navigator.Navigator
import javax.inject.Inject

@ActivityRetainedScoped
class NavigatorImpl @Inject constructor() : Navigator {
    override val backStack: SnapshotStateList<NavKey> = mutableStateListOf(HomeScreenKey.Root)

    override fun navigate(destination: NavKey) {
        backStack.add(destination)
    }

    override fun goBack() {
        backStack.removeLastOrNull()
    }
}