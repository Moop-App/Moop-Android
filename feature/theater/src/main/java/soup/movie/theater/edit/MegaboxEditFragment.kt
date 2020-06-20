package soup.movie.theater.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import dagger.hilt.android.AndroidEntryPoint
import soup.movie.model.Theater
import soup.movie.theater.databinding.TheaterEditChildFragmentBinding

@AndroidEntryPoint
class MegaboxEditFragment : Fragment() {

    private val viewModel: TheaterEditViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = TheaterEditChildFragmentBinding.inflate(inflater, container, false)
        binding.initViewState(viewModel)
        return binding.root
    }

    private fun TheaterEditChildFragmentBinding.initViewState(viewModel: TheaterEditViewModel) {
        val listAdapter = TheaterEditChildListAdapter(object : TheaterEditChildListAdapter.Listener {

            override fun add(theater: Theater): Boolean {
                return viewModel.add(theater)
            }

            override fun remove(theater: Theater) {
                viewModel.remove(theater)
            }
        })
        listView.adapter = listAdapter
        viewModel.megaboxUiModel.observe(viewLifecycleOwner) {
            listAdapter.submitList(it.areaGroupList, it.selectedTheaterIdSet)
        }
    }
}
