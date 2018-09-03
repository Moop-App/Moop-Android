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
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_theater_sort.*
import soup.movie.R
import soup.movie.databinding.ActivityTheaterSortBinding
import soup.movie.ui.BaseActivity
import soup.movie.ui.theater.edit.TheaterEditActivity
import soup.movie.util.delegates.contentView
import soup.widget.recyclerview.listener.OnDragStartListener
import soup.widget.recyclerview.listener.OnItemMoveListener
import soup.widget.recyclerview.util.SimpleItemTouchHelperCallback
import timber.log.Timber
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
        listView.adapter = listAdapter

        //FixMe: find a timing to call startPostponedEnterTransition()
        listView.postOnAnimationDelayed(100) {
            startPostponedEnterTransition()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_theater_sort, menu)
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
        listAdapter.submitList(viewState.selectedTheaters)
    }

    fun onConfirmClicked(view: View) {
        presenter.onConfirmClicked()
        finish()
    }
}
