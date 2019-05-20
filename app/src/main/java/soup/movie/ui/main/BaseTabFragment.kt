package soup.movie.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.annotation.MenuRes
import soup.movie.ui.BaseFragment
import soup.movie.ui.base.SharedElementsMapper

abstract class BaseTabFragment : BaseFragment(), SharedElementsMapper, PanelConsumer {

    @MenuRes
    protected open val menuResource: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(menuResource != null)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menuResource?.let { inflater.inflate(it, menu) }
        super.onCreateOptionsMenu(menu, inflater)
    }
}
