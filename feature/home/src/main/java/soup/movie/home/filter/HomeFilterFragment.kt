package soup.movie.home.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import soup.movie.home.R
import soup.movie.home.databinding.HomeFilterFragmentBinding
import soup.movie.util.inflate

@AndroidEntryPoint
class HomeFilterFragment : Fragment() {

    private lateinit var binding: HomeFilterFragmentBinding

    private val viewModel: HomeFilterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFilterFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.initFilter(viewModel)
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
