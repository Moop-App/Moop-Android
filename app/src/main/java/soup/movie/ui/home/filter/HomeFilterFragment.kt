package soup.movie.ui.home.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import soup.movie.R
import soup.movie.databinding.HomeFilterFragmentBinding
import soup.movie.ui.base.BaseBottomSheetDialogFragment
import soup.movie.util.inflate
import soup.movie.util.observe

class HomeFilterFragment : BaseBottomSheetDialogFragment() {

    private val viewModel: HomeFilterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = HomeFilterFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.initViewState(viewModel)
        return binding.root
    }

    private fun HomeFilterFragmentBinding.initViewState(viewModel: HomeFilterViewModel) {
        viewModel.genreUiModel.observe(viewLifecycleOwner) {
            genreItem.genreFilterGroup.setGenreSet(it)
        }
    }

    private fun ChipGroup.setGenreSet(uiModel: GenreFilterUiModel) {
        removeAllViews()
        uiModel.items.forEach {
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
