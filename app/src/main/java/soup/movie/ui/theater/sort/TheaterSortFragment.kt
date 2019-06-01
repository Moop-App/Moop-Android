package soup.movie.ui.theater.sort

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.listener.OnDragStartListener
import androidx.recyclerview.widget.listener.OnItemMoveListener
import androidx.recyclerview.widget.util.SimpleItemTouchHelperCallback
import androidx.transition.TransitionInflater
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.theater_sort_fragment.*
import soup.movie.R
import soup.movie.databinding.TheaterSortFragmentBinding
import soup.movie.ui.BaseFragment
import soup.movie.ui.base.OnBackPressedListener
import soup.movie.ui.theater.edit.TheaterEditActivity
import soup.movie.util.observe
import soup.movie.util.setOnDebounceClickListener
import soup.movie.util.with
import java.util.concurrent.TimeUnit

class TheaterSortFragment : BaseFragment(), OnBackPressedListener {

    private val viewModel: TheaterSortViewModel by viewModel()

    private val listAdapter: TheaterSortListAdapter by lazy {
        val callback = SimpleItemTouchHelperCallback(object : OnItemMoveListener {
            override fun onItemMove(fromPosition: Int, toPosition: Int) {
                listAdapter.onItemMove(fromPosition, toPosition)
                viewModel.onItemMove(fromPosition, toPosition)
            }
        })
        val itemTouchHelper = ItemTouchHelper(callback).apply {
            attachToRecyclerView(listView)
        }
        TheaterSortListAdapter(object : OnDragStartListener {
            override fun onDragStart(viewHolder: RecyclerView.ViewHolder) {
                itemTouchHelper.startDrag(viewHolder)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition(500, TimeUnit.MILLISECONDS)
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String>,
                                             sharedElements: MutableMap<String, View>) {
                sharedElements.clear()
                listView?.run {
                    (0 until childCount)
                        .mapNotNull { getChildAt(it) }
                        .mapNotNull { it.findViewById<Chip>(R.id.theaterChip) }
                        .forEach { sharedElements[it.transitionName] = it }
                }
            }
        })
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String>,
                                             sharedElements: MutableMap<String, View>) {
                sharedElements.clear()
                names.forEach { name ->
                    listView.findViewWithTag<View>(name)?.run {
                        sharedElements[name] = this
                    }
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return TheaterSortFragmentBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        confirmButton.setOnDebounceClickListener {
            onAddItemClick(it)
        }
        listView.adapter = listAdapter
        viewModel.uiModel.observe(this) {
            render(it)
            //FixMe: find a timing to call startPostponedEnterTransition()
            startPostponedEnterTransition()
        }
    }

    private fun render(uiModel: TheaterSortUiModel) {
        listAdapter.submitList(uiModel.selectedTheaters)
        noItemsView.isVisible = uiModel.selectedTheaters.isEmpty()
    }

    override fun onBackPressed(): Boolean {
        viewModel.saveSnapshot()
        return false
    }

    private fun onAddItemClick(view: View) {
        val intent = Intent(view.context, TheaterEditActivity::class.java)
        startActivityForResult(intent, 0, ActivityOptions
            .makeSceneTransitionAnimation(requireActivity(), *createSharedElements())
            .toBundle())
    }

    private fun createSharedElements(): Array<Pair<View, String>> =
        listView?.run {
            (0 until childCount)
                .mapNotNull { getChildAt(it) }
                .mapNotNull { it.findViewById<Chip>(R.id.theaterChip) }
                .map { it with it.transitionName }
                .toTypedArray()
        } ?: emptyArray()
}
