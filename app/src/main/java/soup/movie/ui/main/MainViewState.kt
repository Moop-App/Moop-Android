package soup.movie.ui.main

sealed class MainViewState {

    object NowState : MainViewState()

    object PlanState : MainViewState()

    object SettingsState : MainViewState()
}
