package soup.movie.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import soup.movie.ui.BaseContract.Presenter
import soup.movie.ui.BaseContract.View
import soup.movie.ui.BaseFragment

abstract class BaseTabFragment<V: View, P: Presenter<V>> :
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

    override fun onDestroy() {
        panelProvider.hidePanel()
        super.onDestroy()
    }

    protected fun showPanel(panelState: PanelData) {
        panelProvider.showPanel(panelState)
    }

    protected fun hidePanel() {
        panelProvider.hidePanel()
    }

    interface OnReselectListener {

        fun onReselect()
    }

    interface PanelProvider {

        fun showPanel(panelState: PanelData)

        fun hidePanel()
    }

    data class PanelData(val tag: String,
                         val newFragment: () -> Fragment)
}
