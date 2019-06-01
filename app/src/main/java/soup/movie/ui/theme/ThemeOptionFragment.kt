package soup.movie.ui.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import soup.movie.databinding.ThemeOptionFragmentBinding
import soup.movie.ui.BaseFragment
import soup.movie.util.observe

class ThemeOptionFragment : BaseFragment() {

    private val viewModel: ThemeOptionViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding = ThemeOptionFragmentBinding.inflate(inflater, container, false)
        val listAdapter = ThemeOptionListAdapter {
            viewModel.onItemClick(it)
        }
        binding.listView.adapter = listAdapter
        viewModel.uiModel.observe(viewLifecycleOwner) {
            listAdapter.submitList(it.items)
        }
        return binding.root
    }
}
