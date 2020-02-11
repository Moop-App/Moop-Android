package soup.movie.ui.theater.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import soup.movie.databinding.TheaterEditChildFragmentBinding
import soup.movie.model.Theater
import soup.movie.ui.base.BaseFragment

class LotteEditFragment : BaseFragment() {

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
        viewModel.lotteUiModel.observe(viewLifecycleOwner) {
            listAdapter.submitList(it.areaGroupList, it.selectedTheaterIdSet)
        }
    }
}
