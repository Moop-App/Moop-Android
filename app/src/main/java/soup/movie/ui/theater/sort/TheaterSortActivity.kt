package soup.movie.ui.theater.sort

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.core.view.postOnAnimationDelayed
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_theater_sort.*
import soup.movie.R
import soup.movie.databinding.ActivityTheaterSortBinding
import soup.movie.ui.BaseActivity
import soup.movie.ui.theater.edit.TheaterEditActivity
import soup.movie.util.delegates.contentView
import soup.movie.util.log.printRenderLog
import soup.movie.util.setVisibleIf
import soup.movie.util.with
import soup.widget.recyclerview.listener.OnDragStartListener
import soup.widget.recyclerview.listener.OnItemMoveListener
import soup.widget.recyclerview.util.SimpleItemTouchHelperCallback
import javax.inject.Inject

class TheaterSortActivity :
        BaseActivity<TheaterSortContract.View, TheaterSortContract.Presenter>(),
        TheaterSortContract.View {

    override val binding by contentView<TheaterSortActivity, ActivityTheaterSortBinding>(
            R.layout.activity_theater_sort
    )

    @Inject
    override lateinit var presenter: TheaterSortContract.Presenter

    private val listAdapter: TheaterSortListAdapter by lazy {
        val callback = SimpleItemTouchHelperCallback(object : OnItemMoveListener {
            override fun onItemMove(fromPosition: Int, toPosition: Int) {
                listAdapter.onItemMove(fromPosition, toPosition)
                presenter.onItemMove(fromPosition, toPosition)
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
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        postponeEnterTransition()

        //FixMe: find a timing to call startPostponedEnterTransition()
        listView.postOnAnimationDelayed(100) {
            startPostponedEnterTransition()
        }
    }

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        listView.adapter = listAdapter

        //FixMe: find a timing to call startPostponedEnterTransition()
        listView.postOnAnimationDelayed(100) {
            startPostponedEnterTransition()
        }
    }

    override fun render(viewState: TheaterSortViewState) {
        printRenderLog { viewState }
        listAdapter.submitList(viewState.selectedTheaters)
        noItemsView.setVisibleIf { viewState.selectedTheaters.isEmpty() }
    }

    override fun onBackPressed() {
        presenter.saveSnapshot()
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
