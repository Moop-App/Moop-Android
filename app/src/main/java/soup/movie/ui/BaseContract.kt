package soup.movie.ui

interface BaseContract {

    interface Presenter<V : BaseContract.View> {

        fun onAttach(view: V)
        fun onDetach()
    }

    interface View
}
