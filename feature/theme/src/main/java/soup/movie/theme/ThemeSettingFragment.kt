package soup.movie.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import soup.movie.theme.databinding.ThemeOptionFragmentBinding

@AndroidEntryPoint
class ThemeSettingFragment : Fragment() {

    private val viewModel: ThemeSettingViewModel by viewModels()

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

    private fun ThemeOptionFragmentBinding.initViewState(viewModel: ThemeSettingViewModel) {
        val listAdapter = ThemeSettingListAdapter {
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
