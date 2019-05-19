package soup.movie.ui.theater.sort

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.core.view.isVisible
import androidx.core.view.postOnAnimationDelayed
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.listener.OnDragStartListener
import androidx.recyclerview.widget.listener.OnItemMoveListener
import androidx.recyclerview.widget.util.SimpleItemTouchHelperCallback
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_theater_sort.*
import soup.movie.R
import soup.movie.databinding.ActivityTheaterSortBinding
import soup.movie.ui.BaseActivity
import soup.movie.ui.theater.edit.TheaterEditActivity
import soup.movie.util.observe
import soup.movie.util.with

class TheaterSortActivity : BaseActivity() {

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

        DataBindingUtil.setContentView<ActivityTheaterSortBinding>(this, R.layout.activity_theater_sort).apply {
            lifecycleOwner = this@TheaterSortActivity
        }

        listView.adapter = listAdapter

        //FixMe: find a timing to call startPostponedEnterTransition()
        listView.postOnAnimationDelayed(100) {
            startPostponedEnterTransition()
        }

        postponeEnterTransition()
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

        viewModel.uiModel.observe(this) {
            render(it)
        }
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        postponeEnterTransition()

        //FixMe: find a timing to call startPostponedEnterTransition()
        listView.postOnAnimationDelayed(100) {
            startPostponedEnterTransition()
        }
    }

    private fun render(uiModel: TheaterSortUiModel) {
        listAdapter.submitList(uiModel.selectedTheaters)
        noItemsView.isVisible = uiModel.selectedTheaters.isEmpty()
    }

    override fun onBackPressed() {
        viewModel.saveSnapshot()
        setResult(RESULT_OK)
        super.onBackPressed()
    }

    fun onAddItemClick(view: View) {
        val intent = Intent(this, TheaterEditActivity::class.java)
        startActivityForResult(intent, 0, ActivityOptions
            .makeSceneTransitionAnimation(this, *createSharedElements())
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
