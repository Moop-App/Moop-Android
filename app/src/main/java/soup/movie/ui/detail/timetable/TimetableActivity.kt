package soup.movie.ui.detail.timetable

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import kotlinx.android.synthetic.main.activity_timetable.*
import kotlinx.android.synthetic.main.activity_timetable.view.*
import soup.movie.R
import soup.movie.data.helper.restoreFrom
import soup.movie.data.helper.saveTo
import soup.movie.data.model.Movie
import soup.movie.databinding.ActivityTimetableBinding
import soup.movie.ui.BaseActivity
import soup.movie.ui.detail.timetable.TimetableViewState.*
import soup.movie.ui.theater.edit.TheaterEditActivity
import soup.movie.util.delegates.contentView
import soup.movie.util.log.printRenderLog
import timber.log.Timber
import javax.inject.Inject

class TimetableActivity :
        BaseActivity<TimetableContract.View, TimetableContract.Presenter>(),
        TimetableContract.View {

    override val binding by contentView<TimetableActivity, ActivityTimetableBinding>(
            R.layout.activity_timetable
    )

    @Inject
    override lateinit var presenter: TimetableContract.Presenter

    private lateinit var listAdapter: TimetableAdapter

    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        movie = if (savedInstanceState == null) {
            intent.restoreFrom()!!
        } else {
            savedInstanceState.restoreFrom()!!
        }
        Timber.d("onCreate: movie=%s", movie)
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        movie.saveTo(outState)
    }

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        listAdapter = TimetableAdapter(ctx)
        listView.apply {
            adapter = listAdapter
        }
        val listener = View.OnClickListener {
            startActivity(Intent(this, TheaterEditActivity::class.java))
        }
        noTheaterView.setOnClickListener(listener)
        noTheaterView.select.setOnClickListener(listener)
    }

    override fun onStart() {
        super.onStart()
        presenter.requestData(movie)
    }

    override fun render(viewState: TimetableViewState) {
        viewState.printRenderLog()
        return when (viewState) {
            is NoTheaterState -> {
                noTheaterView.visibility = VISIBLE
                noResultView.visibility = GONE
                listView.visibility = GONE
            }
            is NoResultState -> {
                noTheaterView.visibility = GONE
                noResultView.visibility = VISIBLE
                listView.visibility = GONE
            }
            is DataState -> {
                noTheaterView.visibility = GONE
                noResultView.visibility = GONE
                listView.visibility = VISIBLE
                listAdapter.submitList(viewState.timeTable.dayList.toMutableList())
            }
        }
    }
}
