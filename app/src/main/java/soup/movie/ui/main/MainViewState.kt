package soup.movie.ui.main

sealed class MainViewState {

    object NowState : MainViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    object PlanState : MainViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    object TheatersState : MainViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    object SettingsState : MainViewState() {

        override fun toString(): String = javaClass.simpleName
    }
}
