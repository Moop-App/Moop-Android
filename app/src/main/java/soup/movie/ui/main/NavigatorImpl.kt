package soup.movie.ui.main

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import dagger.hilt.android.scopes.ActivityRetainedScoped
import soup.movie.feature.navigator.Navigator
import soup.movie.feature.navigator.Screen
import javax.inject.Inject

@ActivityRetainedScoped
class NavigatorImpl @Inject constructor() : Navigator {
    override val backStack: SnapshotStateList<Screen> = mutableStateListOf(Screen.Main)

    override fun navigate(destination: Screen) {
        backStack.add(destination)
    }

    override fun goBack() {
        backStack.removeLastOrNull()
    }
}