package soup.movie.ui.main

import androidx.annotation.Keep

sealed class MainViewState {

    @Keep
    object NowState : MainViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    object PlanState : MainViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    object TheatersState : MainViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    object SettingsState : MainViewState() {

        override fun toString(): String = javaClass.simpleName
    }
}
