package soup.movie.ui.theater.sort

import android.app.SharedElementCallback
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.view.postOnAnimationDelayed
import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.android.synthetic.main.activity_theater_sort.*
import soup.movie.R
import soup.movie.ui.BaseActivity
import soup.movie.ui.theater.edit.TheaterEditActivity
import soup.movie.util.verticalLayoutManager
import soup.widget.recyclerview.util.ItemTouchHelperAdapter
import soup.widget.recyclerview.listener.OnStartDragListener
import soup.widget.recyclerview.util.SimpleItemTouchHelperCallback
import timber.log.Timber
import javax.inject.Inject

class TheaterSortActivity :
        BaseActivity<TheaterSortContract.View, TheaterSortContract.Presenter>(),
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
            override fun onMapSharedElements(names: List<String>,
                                             sharedElements: MutableMap<String, View>) =
                    names.forEach { name ->
                        listView.findViewWithTag<View>(name)?.run {
                            sharedElements[name] = this
                        }
                    }
        })
    }

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        listView.layoutManager = ctx.verticalLayoutManager()

        //FixMe: find a timing to call startPostponedEnterTransition()
        listView.postOnAnimationDelayed(100) {
            startPostponedEnterTransition()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.sort_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_edit) {
            startActivity(Intent(this, TheaterEditActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
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
        //FixMe: find a timing to call startPostponedEnterTransition()
        //startPostponedEnterTransition()
    }

    fun onCancelClicked(view: View) {
        onBackPressed()
    }

    fun onConfirmClicked(view: View) {
        presenter.onConfirmClicked(listAdapter.theaters)
        onBackPressed()
    }
}
