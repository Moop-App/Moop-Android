package soup.movie.ui.main.settings.help

import soup.movie.ui.BaseContract

interface HelpContract {

    interface Presenter : BaseContract.Presenter<View>

    interface View : BaseContract.View {

        fun render(viewState: HelpViewState)
    }
}
