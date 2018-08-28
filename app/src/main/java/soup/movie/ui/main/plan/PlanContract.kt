package soup.movie.ui.main.plan

import soup.movie.ui.BaseContract

interface PlanContract {

    interface Presenter : BaseContract.Presenter<View> {

        fun refresh()
    }

    interface View : BaseContract.View {

        fun render(viewState: PlanViewState)
    }
}
