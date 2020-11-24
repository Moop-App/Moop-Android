package soup.movie.theater.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.listener.OnDragListener
import androidx.recyclerview.widget.listener.OnItemMoveListener
import androidx.recyclerview.widget.util.SimpleItemTouchHelperCallback
import androidx.transition.TransitionInflater
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import soup.movie.theater.R
import soup.movie.theater.databinding.TheaterSortFragmentBinding
import soup.movie.ui.base.OnBackPressedListener
import soup.movie.util.autoCleared
import soup.movie.util.setOnDebounceClickListener
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class TheaterSortFragment : Fragment(R.layout.theater_sort_fragment), OnBackPressedListener {

    private var binding: TheaterSortFragmentBinding by autoCleared()

    private val viewModel: TheaterSortViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String>,
                                             sharedElements: MutableMap<String, View>) {
                sharedElements.clear()
                binding.listView.run {
                    (0 until childCount)
                        .mapNotNull { getChildAt(it) }
                        .mapNotNull { it.findViewById<Chip>(R.id.theaterChip) }
                        .forEach { sharedElements[it.transitionName] = it }
                }
                Timber.d("setEnterSharedElementCallback: ${sharedElements.keys}")
            }
        })
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String>,
                                             sharedElements: MutableMap<String, View>) {
                sharedElements.clear()
                names.forEach { name ->
                    binding.listView.findViewWithTag<View>(name)?.run {
                        sharedElements[name] = this
                    }
                }
                Timber.d("setExitSharedElementCallback: ${sharedElements.keys}")
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postponeEnterTransition(400, TimeUnit.MILLISECONDS)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = TheaterSortFragmentBinding.bind(view).apply {
            initViewState(viewModel)
            adaptSystemWindowInset()
        }
    }

    private fun TheaterSortFragmentBinding.adaptSystemWindowInset() {
        Insetter.builder()
            .setOnApplyInsetsListener { view, insets, initialState ->
                view.updatePadding(
                    top = initialState.paddings.top + insets.systemWindowInsetTop
                )
            }
            .applyToView(theaterSortScene)
        Insetter.builder()
            .setOnApplyInsetsListener { view, insets, initialState ->
                view.updatePadding(
                    bottom = initialState.paddings.bottom + insets.systemWindowInsetBottom
                )
            }
            .applyToView(container)
    }

    private fun TheaterSortFragmentBinding.initViewState(viewModel: TheaterSortViewModel) {
        confirmButton.setOnDebounceClickListener {
            onAddItemClick()
        }

        val listAdapter = TheaterSortListAdapter()
        val callback = SimpleItemTouchHelperCallback(object : OnItemMoveListener {
            override fun onItemMove(fromPosition: Int, toPosition: Int) {
                listAdapter.onItemMove(fromPosition, toPosition)
                viewModel.onItemMove(fromPosition, toPosition)
            }
        })
        val itemTouchHelper = ItemTouchHelper(callback).apply {
            attachToRecyclerView(listView)
        }
        listAdapter.setOnDragListener(object : OnDragListener {
            override fun onDragStart(viewHolder: RecyclerView.ViewHolder) {
                itemTouchHelper.startDrag(viewHolder)
            }
        })
        listView.adapter = listAdapter
        viewModel.uiModel.observe(viewLifecycleOwner) {
            listAdapter.submitList(it.selectedTheaters)
            noItemsView.isVisible = it.selectedTheaters.isEmpty()
        }
    }

    override fun onBackPressed(): Boolean {
        viewModel.saveSnapshot()
        return false
    }

    private fun onAddItemClick() {
        findNavController().navigate(
            TheaterSortFragmentDirections.actionToTheaterEdit(),
            FragmentNavigatorExtras(*createSharedElements())
        )
    }

    private fun createSharedElements(): Array<Pair<View, String>> =
        binding.listView.run {
            (0 until childCount)
                .mapNotNull { getChildAt(it) }
                .mapNotNull { it.findViewById(R.id.theaterChip) }
                .map { Pair(it, it.transitionName) }
                .toTypedArray()
        }
}
