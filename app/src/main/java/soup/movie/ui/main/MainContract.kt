package soup.movie.ui.main

import soup.movie.settings.ui.MainTabSetting
import soup.movie.ui.BaseContract

interface MainContract {

    interface Presenter : BaseContract.Presenter<View> {

        fun setCurrentTab(mode: MainTabSetting.Tab)
    }

    interface View : BaseContract.View {

        fun render(viewState: MainViewState)
    }
}
