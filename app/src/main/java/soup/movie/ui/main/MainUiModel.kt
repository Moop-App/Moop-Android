package soup.movie.ui.main

import androidx.annotation.Keep

sealed class MainUiModel {

    @Keep
    object NowState : MainUiModel()

    @Keep
    object PlanState : MainUiModel()

    @Keep
    object SettingsState : MainUiModel()
}
