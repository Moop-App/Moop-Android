package soup.movie.ui.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.lifecycle.observe
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import soup.movie.databinding.ThemeOptionFragmentBinding
import soup.movie.ui.base.BaseFragment

class ThemeOptionFragment : BaseFragment() {

    private val viewModel: ThemeOptionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ThemeOptionFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.initViewState(viewModel)
        binding.adaptSystemWindowInset()
        return binding.root
    }

    private fun ThemeOptionFragmentBinding.initViewState(viewModel: ThemeOptionViewModel) {
        val listAdapter = ThemeOptionListAdapter {
            viewModel.onItemClick(it)
        }
        listView.adapter = listAdapter
        viewModel.uiModel.observe(viewLifecycleOwner) {
            listAdapter.submitList(it.items)
        }
    }

    private fun ThemeOptionFragmentBinding.adaptSystemWindowInset() {
        themeOptionScene.doOnApplyWindowInsets { themeOptionScene, insets, initialState ->
            themeOptionScene.updatePadding(
                top = initialState.paddings.top + insets.systemWindowInsetTop
            )
        }
        listView.doOnApplyWindowInsets { listView, insets, initialState ->
            listView.updatePadding(
                bottom = initialState.paddings.bottom + insets.systemWindowInsetBottom
            )
        }
    }
}
