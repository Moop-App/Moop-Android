package soup.movie.ui.main

import soup.movie.settings.impl.LastMainTabSetting
import soup.movie.ui.BaseContract

interface MainContract {

    interface Presenter : BaseContract.Presenter<View> {

        fun setCurrentTab(mode: LastMainTabSetting.Tab)
    }

    interface View : BaseContract.View {

        fun render(viewState: MainViewState)
    }
}
