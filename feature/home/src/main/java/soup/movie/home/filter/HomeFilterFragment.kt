package soup.movie.home.filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import soup.movie.home.R
import soup.movie.home.databinding.HomeFilterFragmentBinding
import soup.movie.util.inflate
import soup.movie.util.setOnDebounceClickListener

@AndroidEntryPoint
class HomeFilterFragment : Fragment(R.layout.home_filter_fragment) {

    private val viewModel: HomeFilterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(HomeFilterFragmentBinding.bind(view)) {
            theaterItem.cgvView.setOnCheckedChangeListener { filterChip, isChecked ->
                viewModel.onCgvFilterChanged(isChecked)
            }
            theaterItem.lotteView.setOnCheckedChangeListener { filterChip, isChecked ->
                viewModel.onLotteFilterChanged(isChecked)
            }
            theaterItem.megaboxView.setOnCheckedChangeListener { filterChip, isChecked ->
                viewModel.onMegaboxFilterChanged(isChecked)
            }
            viewModel.theaterUiModel.observe(viewLifecycleOwner) {
                theaterItem.cgvView.isChecked = it.hasCgv
                theaterItem.lotteView.isChecked = it.hasLotteCinema
                theaterItem.megaboxView.isChecked = it.hasMegabox
            }

            ageItem.ageAllView.setOnDebounceClickListener {
                viewModel.onAgeAllFilterClicked()
            }
            ageItem.age12View.setOnDebounceClickListener {
                viewModel.onAge12FilterClicked()
            }
            ageItem.age15View.setOnDebounceClickListener {
                viewModel.onAge15FilterClicked()
            }
            ageItem.age19View.setOnDebounceClickListener {
                viewModel.onAge19FilterClicked()
            }
            viewModel.ageUiModel.observe(viewLifecycleOwner) {
                ageItem.ageAllView.isSelected = it.hasAll
                ageItem.age12View.isSelected = it.has12
                ageItem.age15View.isSelected = it.has15
                ageItem.age19View.isSelected = it.has19
            }

            viewModel.genreUiModel.observe(viewLifecycleOwner) {
                genreItem.genreFilterGroup.run {
                    removeAllViews()
                    it.items.forEach {
                        val genreChip: Chip = inflate(context, R.layout.home_filter_item_genre)
                        genreChip.text = it.name
                        genreChip.isChecked = it.isChecked
                        genreChip.setOnCheckedChangeListener { _, isChecked ->
                            viewModel.onGenreFilterClick(it.name, isChecked)
                        }
                        addView(genreChip)
                    }
                }
            }
        }
    }
}
