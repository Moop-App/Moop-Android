package soup.movie.ui.theater.edit.megabox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.theater_edit_fragment_child.*
import soup.movie.data.model.Theater
import soup.movie.databinding.TheaterEditFragmentChildBinding
import soup.movie.ui.theater.edit.TheaterEditChildFragment
import soup.movie.ui.theater.edit.TheaterEditChildListAdapter
import soup.movie.util.observe

class MegaboxEditFragment : TheaterEditChildFragment() {

    override val title: String = "메가박스"

    private val viewModel: MegaboxEditViewModel by viewModel()

    private val listAdapter: TheaterEditChildListAdapter by lazy {
        TheaterEditChildListAdapter(object : TheaterEditChildListAdapter.Listener {

            override fun add(theater: Theater): Boolean {
                return viewModel.add(theater)
            }

            override fun remove(theater: Theater) {
                viewModel.remove(theater)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return TheaterEditFragmentChildBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView.adapter = listAdapter
        viewModel.uiModel.observe(viewLifecycleOwner) {
            listAdapter.submitList(it.areaGroupList, it.selectedTheaterIdSet)
        }
    }

    companion object {

        fun newInstance() = MegaboxEditFragment()
    }
}
