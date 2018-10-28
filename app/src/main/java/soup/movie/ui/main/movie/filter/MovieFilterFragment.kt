package soup.movie.ui.main.movie.filter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_filter_theater.*
import soup.movie.databinding.FragmentMovieFilterBinding
import soup.movie.ui.BaseFragment
import soup.movie.ui.main.BaseTabFragment.PanelData
import soup.movie.util.log.printRenderLog
import javax.inject.Inject

class MovieFilterFragment :
        BaseFragment<MovieFilterContract.View, MovieFilterContract.Presenter>(),
        MovieFilterContract.View {

    @Inject
    override lateinit var presenter: MovieFilterContract.Presenter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            FragmentMovieFilterBinding.inflate(inflater, container, false).root

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        cgvView.setOnCheckedChangeListener { _, isChecked ->
            presenter.onCgvFilterChanged(isChecked)
        }
        lotteView.setOnCheckedChangeListener { _, isChecked ->
            presenter.onLotteFilterChanged(isChecked)
        }
        megaboxView.setOnCheckedChangeListener { _, isChecked ->
            presenter.onMegaboxFilterChanged(isChecked)
        }
    }

    override fun render(viewState: MovieFilterViewState) {
        printRenderLog { viewState }
        val filter = viewState.filter
        cgvView.isChecked = filter.hasCgv()
        lotteView.isChecked = filter.hasLotteCinema()
        megaboxView.isChecked = filter.hasMegabox()
    }

    companion object {

        private fun newInstance(): MovieFilterFragment = MovieFilterFragment()

        fun toPanelData() = PanelData(toString()) { newInstance() }
    }
}
