package soup.movie.ui.main.home.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.home_filter_item_genre.*
import soup.movie.R
import soup.movie.databinding.HomeFilterFragmentBinding
import soup.movie.ui.BaseBottomSheetDialogFragment
import soup.movie.util.inflate
import soup.movie.util.observe

class HomeFilterFragment : BaseBottomSheetDialogFragment() {

    private val viewModel: HomeFilterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return HomeFilterFragmentBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = this@HomeFilterFragment.viewModel
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.genreUiModel.observe(viewLifecycleOwner) {
            genreFilterGroup?.setGenreSet(it)
        }
    }

    private fun ChipGroup.setGenreSet(uiModel: GenreFilterUiModel) {
        removeAllViews()
        uiModel.items.forEach {
            val genreChip: Chip = inflate(context, R.layout.chip_filter_genre)
            genreChip.text = it.name
            genreChip.isChecked = it.isChecked
            genreChip.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onGenreFilterClick(it.name, isChecked)
            }
            addView(genreChip)
        }
    }

    companion object {

        fun show(hostFragment: Fragment) {
            hostFragment.fragmentManager?.run {
                HomeFilterFragment().show(this, "HomeFilter")
            }
        }
    }
}
