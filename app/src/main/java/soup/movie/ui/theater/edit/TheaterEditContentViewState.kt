package soup.movie.ui.theater.edit

import androidx.annotation.Keep

sealed class TheaterEditContentViewState {

    @Keep
    object LoadingState : TheaterEditContentViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    object ErrorState : TheaterEditContentViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    object DoneState : TheaterEditContentViewState() {

        override fun toString(): String = javaClass.simpleName
    }
}
