package soup.movie.ui.main.settings

import soup.movie.ui.BaseContract

interface SettingsContract {

    interface Presenter : BaseContract.Presenter<View>

    interface View : BaseContract.View {

        fun render(viewState: SettingsViewState)
    }
}
