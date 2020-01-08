package soup.movie.ui.home.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import com.google.android.material.chip.Chip
import soup.movie.R
import soup.movie.databinding.HomeFilterFragmentBinding
import soup.movie.ui.base.BaseFragment
import soup.movie.util.inflate

class HomeFilterFragment : BaseFragment() {

    private lateinit var binding: HomeFilterFragmentBinding

    private val filterViewModel: HomeFilterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFilterFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = filterViewModel
        binding.initFilter(filterViewModel)
        return binding.root
    }

    private fun HomeFilterFragmentBinding.initFilter(viewModel: HomeFilterViewModel) {
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
