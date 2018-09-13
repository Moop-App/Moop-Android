package soup.movie.ui.main.theaters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import soup.movie.databinding.FragmentTheatersBinding
import soup.movie.ui.main.BaseTabFragment
import soup.movie.util.log.printRenderLog
import javax.inject.Inject

class TheatersFragment :
        BaseTabFragment<TheatersContract.View, TheatersContract.Presenter>(),
        TheatersContract.View {

    @Inject
    override lateinit var presenter: TheatersContract.Presenter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            FragmentTheatersBinding.inflate(inflater, container, false).root

    override fun render(viewState: TheatersViewState) {
        printRenderLog { viewState }
    }

    companion object {

        fun newInstance(): TheatersFragment = TheatersFragment()
    }
}
