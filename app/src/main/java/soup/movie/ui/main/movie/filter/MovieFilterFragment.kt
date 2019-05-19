package soup.movie.ui.main.movie.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_filter_age.*
import kotlinx.android.synthetic.main.item_filter_theater.*
import soup.movie.databinding.FragmentMovieFilterBinding
import soup.movie.ui.BaseFragment
import soup.movie.ui.main.PanelData
import soup.movie.util.observe

class MovieFilterFragment : BaseFragment() {

    private val viewModel: MovieFilterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentMovieFilterBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewState()
        viewModel.theaterUiModel.observe(this) {
            val filter = it.filter
            cgvView.isChecked = filter.hasCgv()
            lotteView.isChecked = filter.hasLotteCinema()
            megaboxView.isChecked = filter.hasMegabox()
        }
        viewModel.ageUiModel.observe(this) {
            val filter = it.filter
            ageAllView.isSelected = filter.hasAll()
            age12View.isSelected = filter.has12()
            age15View.isSelected = filter.has15()
            age19View.isSelected = filter.has19()
        }
    }

    private fun initViewState() {
        cgvView.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onCgvFilterChanged(isChecked)
        }
        lotteView.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onLotteFilterChanged(isChecked)
        }
        megaboxView.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onMegaboxFilterChanged(isChecked)
        }
        ageAllView.setOnClickListener {
            viewModel.onAgeAllFilterClicked()
        }
        age12View.setOnClickListener {
            viewModel.onAge12FilterClicked()
        }
        age15View.setOnClickListener {
            viewModel.onAge15FilterClicked()
        }
        age19View.setOnClickListener {
            viewModel.onAge19FilterClicked()
        }
    }

    companion object {

        private fun newInstance(): MovieFilterFragment = MovieFilterFragment()

        fun toPanelData() = PanelData(toString()) { newInstance() }
    }
}
