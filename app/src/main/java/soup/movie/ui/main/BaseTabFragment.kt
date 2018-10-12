package soup.movie.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import soup.movie.ui.BaseContract
import soup.movie.ui.BaseFragment

abstract class BaseTabFragment<V: BaseContract.View, P: BaseContract.Presenter<V>> :
        BaseFragment<V, P>() {

    private lateinit var panelProvider: PanelProvider

    @MenuRes
    protected open val menuResource: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        panelProvider = activity as PanelProvider
        setHasOptionsMenu(menuResource != null)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menuResource?.let { inflater?.inflate(it, menu) }
        super.onCreateOptionsMenu(menu, inflater)
    }

    protected fun showPanel(panelState: PanelData) {
        panelProvider.showPanel(panelState)
    }

    protected fun hidePanel() {
        panelProvider.hidePanel()
    }

    protected fun panelIsShown(): Boolean {
        return panelProvider.panelIsShown()
    }

    open fun onMapSharedElements(names: List<String>,
                                           sharedElements: MutableMap<String, View>) {
        // do nothing
    }

    interface OnReselectListener {

        fun onReselect()
    }

    interface PanelProvider {

        fun showPanel(panelState: PanelData)

        fun hidePanel()

        fun panelIsShown(): Boolean
    }

    data class PanelData(val tag: String,
                         val newFragment: () -> Fragment)
}
