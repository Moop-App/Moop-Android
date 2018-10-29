package soup.movie.ui.main.movie.filter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_filter_age.*
import kotlinx.android.synthetic.main.item_filter_theater.*
import soup.movie.databinding.FragmentMovieFilterBinding
import soup.movie.ui.BaseFragment
import soup.movie.ui.main.BaseTabFragment.PanelData
import soup.movie.ui.main.movie.filter.MovieFilterViewState.AgeFilterViewState
import soup.movie.ui.main.movie.filter.MovieFilterViewState.TheaterFilterViewState
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
        ageAllView.setOnClickListener {
            presenter.onAgeAllFilterClicked()
        }
        age12View.setOnClickListener {
            presenter.onAge12FilterClicked()
        }
        age15View.setOnClickListener {
            presenter.onAge15FilterClicked()
        }
        age19View.setOnClickListener {
            presenter.onAge19FilterClicked()
        }
    }

    override fun render(viewState: TheaterFilterViewState) {
        printRenderLog { viewState }
        val filter = viewState.filter
        cgvView.isChecked = filter.hasCgv()
        lotteView.isChecked = filter.hasLotteCinema()
        megaboxView.isChecked = filter.hasMegabox()
    }

    override fun render(viewState: AgeFilterViewState) {
        printRenderLog { viewState }
        val filter = viewState.filter
        ageAllView.isSelected = filter.hasAll()
        age12View.isSelected = filter.has12()
        age15View.isSelected = filter.has15()
        age19View.isSelected = filter.has19()
    }

    companion object {

        private fun newInstance(): MovieFilterFragment = MovieFilterFragment()

        fun toPanelData() = PanelData(toString()) { newInstance() }
    }
}
