package soup.movie.ui.main.settings

import soup.movie.ui.LegacyBaseContract

interface SettingsContract {

    interface Presenter : LegacyBaseContract.Presenter<View>

    interface View : LegacyBaseContract.View {

        fun render(viewState: SettingsViewState.TheaterListViewState)

        fun render(viewState: SettingsViewState.VersionViewState)
    }
}
