package soup.movie.ui.main

sealed class MainViewState {

    data class NowState(val isVerticalType: Boolean) : MainViewState()

    data class PlanState(val isVerticalType: Boolean) : MainViewState()

    object SettingsState : MainViewState()
}
