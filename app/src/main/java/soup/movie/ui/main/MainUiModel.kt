package soup.movie.ui.main

import androidx.annotation.Keep

sealed class MainUiModel {

    @Keep
    object NowState : MainUiModel() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    object PlanState : MainUiModel() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    object SettingsState : MainUiModel() {

        override fun toString(): String = javaClass.simpleName
    }
}
