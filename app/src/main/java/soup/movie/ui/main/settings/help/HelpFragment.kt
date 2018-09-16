package soup.movie.ui.main.settings.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import soup.movie.databinding.FragmentHelpBinding
import soup.movie.ui.BaseFragment
import soup.movie.ui.main.BaseTabFragment.PanelData
import soup.movie.util.log.printRenderLog
import javax.inject.Inject

class HelpFragment :
        BaseFragment<HelpContract.View, HelpContract.Presenter>(),
        HelpContract.View {

    @Inject
    override lateinit var presenter: HelpContract.Presenter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            FragmentHelpBinding.inflate(inflater, container, false).root

    override fun render(viewState: HelpViewState) {
        printRenderLog { viewState }
    }

    companion object {

        private fun newInstance(): HelpFragment = HelpFragment()

        fun toPanelData() = PanelData(toString()) { newInstance() }
    }
}
