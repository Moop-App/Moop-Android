package soup.movie.theater.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import dagger.android.support.DaggerFragment
import soup.movie.ext.assistedActivityViewModels
import soup.movie.model.Theater
import soup.movie.theater.databinding.TheaterEditChildFragmentBinding
import javax.inject.Inject

class CgvEditFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: TheaterEditViewModel.Factory
    private val viewModel: TheaterEditViewModel by assistedActivityViewModels {
        viewModelFactory.create()
    }

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
        viewModel.cgvUiModel.observe(viewLifecycleOwner) {
            listAdapter.submitList(it.areaGroupList, it.selectedTheaterIdSet)
        }
    }
}
