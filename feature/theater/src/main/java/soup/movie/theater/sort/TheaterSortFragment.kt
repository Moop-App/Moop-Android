package soup.movie.theater.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.observe
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.listener.OnDragListener
import androidx.recyclerview.widget.listener.OnItemMoveListener
import androidx.recyclerview.widget.util.SimpleItemTouchHelperCallback
import androidx.transition.TransitionInflater
import com.google.android.material.chip.Chip
import dagger.android.support.DaggerFragment
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import soup.movie.ext.assistedViewModels
import soup.movie.theater.R
import soup.movie.theater.databinding.TheaterSortFragmentBinding
import soup.movie.ui.base.OnBackPressedListener
import soup.movie.util.setOnDebounceClickListener
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TheaterSortFragment : DaggerFragment(), OnBackPressedListener {

    private lateinit var binding: TheaterSortFragmentBinding

    @Inject
    lateinit var viewModelFactory: TheaterSortViewModel.Factory
    private val viewModel: TheaterSortViewModel by assistedViewModels {
        viewModelFactory.create()
    }

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
        binding = TheaterSortFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.initViewState(viewModel)
        binding.adaptSystemWindowInset()
        return binding.root
    }

    private fun TheaterSortFragmentBinding.adaptSystemWindowInset() {
        theaterSortScene.doOnApplyWindowInsets { theaterSortScene, insets, initialState ->
            theaterSortScene.updatePadding(
                top = initialState.paddings.top + insets.systemWindowInsetTop
            )
        }
        container.doOnApplyWindowInsets { container, insets, initialState ->
            container.updatePadding(
                bottom = initialState.paddings.bottom + insets.systemWindowInsetBottom
            )
        }
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
                .mapNotNull { it.findViewById<View>(R.id.theaterChip) }
                .map { Pair(it, it.transitionName) }
                .toTypedArray()
        }
}
