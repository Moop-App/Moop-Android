package soup.movie.ui.main.settings

import soup.movie.ui.BaseContract

interface SettingsContract {

    interface Presenter : BaseContract.Presenter<View> {

        fun setUseWebLink(enabled: Boolean)
    }

    interface View : BaseContract.View {

        fun render(viewState: SettingsViewState.TheaterListViewState)

        fun render(viewState: SettingsViewState.ExperimentalViewState)

        fun render(viewState: SettingsViewState.VersionViewState)
    }
}
