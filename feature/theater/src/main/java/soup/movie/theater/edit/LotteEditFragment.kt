package soup.movie.theater.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import soup.movie.model.Theater
import soup.movie.theater.R
import soup.movie.theater.databinding.TheaterEditChildFragmentBinding

@AndroidEntryPoint
class LotteEditFragment : Fragment(R.layout.theater_edit_child_fragment) {

    private val viewModel: TheaterEditViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(TheaterEditChildFragmentBinding.bind(view)) {
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
}
