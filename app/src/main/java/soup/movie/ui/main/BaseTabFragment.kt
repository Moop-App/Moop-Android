package soup.movie.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.annotation.MenuRes
import soup.movie.ui.LegacyBaseContract
import soup.movie.ui.LegacyBaseFragment
import soup.movie.ui.base.SharedElementsMapper

abstract class BaseTabFragment<V: LegacyBaseContract.View, P: LegacyBaseContract.Presenter<V>> :
    LegacyBaseFragment<V, P>(), SharedElementsMapper, PanelConsumer {

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
