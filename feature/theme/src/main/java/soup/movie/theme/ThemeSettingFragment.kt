package soup.movie.theme

import android.os.Bundle
import android.view.View
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import soup.movie.theme.databinding.ThemeOptionFragmentBinding

@AndroidEntryPoint
class ThemeSettingFragment : Fragment(R.layout.theme_option_fragment) {

    private val viewModel: ThemeSettingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(ThemeOptionFragmentBinding.bind(view)) {
            initViewState(viewModel)
            adaptSystemWindowInset()
        }
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
