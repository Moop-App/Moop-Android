package soup.movie.ui.main.now

import soup.movie.ui.BaseContract

interface NowContract {

    interface Presenter : BaseContract.Presenter<View> {

        fun refresh()
    }

    interface View : BaseContract.View {

        fun render(viewState: NowViewState)
    }
}
