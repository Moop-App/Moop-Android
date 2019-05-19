package soup.movie.ui.theater.edit.cgv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_theater_edit.*
import soup.movie.data.model.Theater
import soup.movie.databinding.FragmentTheaterEditBinding
import soup.movie.ui.theater.edit.TheaterEditChildListAdapter
import soup.movie.ui.theater.edit.TheaterEditChildFragment
import soup.movie.util.observe

class CgvEditFragment : TheaterEditChildFragment() {

    override val title: String = "CGV"

    private val viewModel: CgvEditViewModel by viewModel()

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
        return FragmentTheaterEditBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView.adapter = listAdapter
        viewModel.uiModel.observe(this) {
            listAdapter.submitList(it.areaGroupList, it.selectedTheaterIdSet)
        }
    }

    companion object {

        fun newInstance() = CgvEditFragment()
    }
}
