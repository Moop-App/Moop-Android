package soup.movie.ui.detail.timetable

import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_timetable.*
import kotlinx.android.synthetic.main.activity_timetable.view.*
import soup.movie.R
import soup.movie.data.helper.Cgv
import soup.movie.data.helper.restoreFrom
import soup.movie.data.helper.saveTo
import soup.movie.data.model.Movie
import soup.movie.data.model.Theater
import soup.movie.data.model.TheaterWithTimetable
import soup.movie.databinding.ActivityTimetableBinding
import soup.movie.ui.BaseActivity
import soup.movie.ui.detail.timetable.TimetableViewState.DoneState
import soup.movie.ui.detail.timetable.TimetableViewState.LoadingState
import soup.movie.ui.theater.edit.TheaterEditActivity
import soup.movie.util.delegates.contentView
import soup.movie.util.log.printRenderLog
import soup.movie.util.setVisibleIf
import soup.movie.util.showIf
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

    private lateinit var theaterListAdapter: TimetableTheaterListAdapter

    private lateinit var dateListAdapter: TimetableDateListAdapter

    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        movie = if (savedInstanceState == null) {
            intent.restoreFrom()!!
        } else {
            savedInstanceState.restoreFrom()!!
        }
        Timber.d("onCreate: movie=%s", movie)
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        titleView.text = movie.title
        binding.item = movie
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        movie.saveTo(outState)
    }

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        dateListAdapter = TimetableDateListAdapter{
            presenter.onItemClick(it)
        }
        dateListView.apply {
            adapter = dateListAdapter
        }
        theaterListAdapter = TimetableTheaterListAdapter(object : TimetableTheaterListAdapter.Listener {

            override fun onItemClick(item: TheaterWithTimetable) {
                presenter.onItemClick(item)
            }

            override fun onItemClick(item: Theater) {
                Cgv.executeAppForSchedule(ctx)
            }

            override fun onItemClick(item: String) {
                //TODO: show notification with selected date and time
                Cgv.executeAppForSchedule(ctx)
            }
        })
        theaterListView.apply {
            adapter = theaterListAdapter
        }
        noTheaterView.selectView.setOnClickListener {
            startActivity(Intent(this, TheaterEditActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.requestData(movie)
    }

    override fun render(viewState: TimetableViewState) {
        printRenderLog { viewState }
        loadingView.showIf { viewState is LoadingState }
        noTheaterView.setVisibleIf { viewState.hasNoTheaters() }
        if (viewState is DoneState) {
            dateListAdapter.submitList(viewState.screeningDateList)
            theaterListAdapter.submitList(viewState.theaterList)
        }
    }
}
