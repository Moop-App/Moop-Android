package soup.movie.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.annotation.MenuRes

import soup.movie.ui.BaseContract.Presenter
import soup.movie.ui.BaseContract.View
import soup.movie.ui.BaseFragment

abstract class BaseTabFragment<V: View, P: Presenter<V>>
    : BaseFragment<V, P>() {

    protected val menuResource: Int
        @MenuRes
        get() = INVALID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(menuResource != INVALID)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        val menuRes = menuResource
        if (menuRes != INVALID) {
            inflater!!.inflate(menuRes, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {

        private const val INVALID = 0
    }
}
