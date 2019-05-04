package soup.movie.ui

interface LegacyBaseContract {

    interface Presenter<V : View> {

        fun onAttach(view: V)
        fun onDetach()
    }

    interface View
}
