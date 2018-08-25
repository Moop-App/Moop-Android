package soup.movie.ui.main

import android.support.annotation.IntDef

import soup.movie.ui.BaseContract

interface MainContract {

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(TAB_MODE_NOW, TAB_MODE_PLAN, TAB_MODE_SETTINGS)
    annotation class TabMode

    interface Presenter : BaseContract.Presenter<View> {
        fun setTabMode(@TabMode mode: Int)
    }

    interface View : BaseContract.View {
        fun render(viewState: MainViewState)
    }

    companion object {

        const val TAB_MODE_NOW = 10
        const val TAB_MODE_PLAN = 20
        const val TAB_MODE_SETTINGS = 30
    }
}
