package soup.movie.ui.theater.sort

import android.app.SharedElementCallback
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_theater_sort.*
import soup.movie.R
import soup.movie.ui.BaseActivity
import soup.movie.ui.theater.edit.TheaterEditActivity
import soup.movie.util.RecyclerViewUtil.verticalLinearLayoutManager
import soup.widget.drag.ItemTouchHelperAdapter
import soup.widget.drag.OnStartDragListener
import soup.widget.drag.SimpleItemTouchHelperCallback
import timber.log.Timber
import javax.inject.Inject

class TheaterSortActivity
    : BaseActivity<TheaterSortContract.View, TheaterSortContract.Presenter>(),
        TheaterSortContract.View {

    @Inject
    override lateinit var presenter: TheaterSortContract.Presenter

    private lateinit var listAdapter: TheaterSortListAdapter

    override val layoutRes: Int
        get() = R.layout.activity_theater_sort

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String>, sharedElements: MutableMap<String, View>) {
                for (name in names) {
                    val child = listView.findViewWithTag<View>(name)
                    sharedElements[name] = child
                }
            }
        })
    }

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        listView.layoutManager = verticalLinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.sort_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                startActivity(Intent(this, TheaterEditActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun render(viewState: TheaterSortViewState) {
        Timber.d("render: %s", viewState)
        val adapterDelegate = object : ItemTouchHelperAdapter {
            override fun onItemMove(fromPosition: Int, toPosition: Int) {
                listAdapter.onItemMove(fromPosition, toPosition)
            }

            override fun onItemDismiss(position: Int) {
                listAdapter.onItemDismiss(position)
            }
        }
        val callback = SimpleItemTouchHelperCallback(adapterDelegate)
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(listView)

        listAdapter = TheaterSortListAdapter(
                viewState.selectedTheaters,
                OnStartDragListener { itemTouchHelper.startDrag(it) })
        listView.adapter = listAdapter
        startPostponedEnterTransition()
    }

    fun onCancelClicked(view: View) {
        onBackPressed()
    }

    fun onConfirmClicked(view: View) {
        presenter.onConfirmClicked(listAdapter.selectedTheaters)
        onBackPressed()
    }
}
