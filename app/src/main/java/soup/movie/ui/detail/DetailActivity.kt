package soup.movie.ui.detail

import android.animation.ValueAnimator
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Pair
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail_header.*
import soup.movie.R
import soup.movie.data.MovieSelectManager
import soup.movie.data.helper.*
import soup.movie.data.model.Movie
import soup.movie.data.model.Theater.Companion.TYPE_CGV
import soup.movie.data.model.Theater.Companion.TYPE_LOTTE
import soup.movie.data.model.Theater.Companion.TYPE_MEGABOX
import soup.movie.data.model.Theater.Companion.TYPE_NONE
import soup.movie.databinding.ActivityDetailBinding
import soup.movie.settings.impl.UseWebLinkSetting
import soup.movie.spec.share
import soup.movie.theme.util.getColorAttr
import soup.movie.ui.BaseActivity
import soup.movie.ui.detail.DetailViewState.*
import soup.movie.ui.detail.timetable.TimetableActivity
import soup.movie.ui.helper.EventAnalytics
import soup.movie.util.delegates.contentView
import soup.movie.util.loadAsync
import soup.movie.util.log.printRenderLog
import soup.movie.util.setBackgroundColorResource
import soup.movie.util.setOnDebounceClickListener
import soup.movie.util.with
import soup.widget.elastic.ElasticDragDismissFrameLayout.SystemChromeFader
import soup.widget.util.AnimUtils.getFastOutSlowInInterpolator
import soup.widget.util.ColorUtils
import soup.widget.util.ViewUtils
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.max

class DetailActivity :
        BaseActivity<DetailContract.View, DetailContract.Presenter>(),
        DetailContract.View {

    override val binding by contentView<DetailActivity, ActivityDetailBinding>(
            R.layout.activity_detail
    )

    private var windowBackground: Int = Color.WHITE

    private lateinit var movie: Movie

    @Inject
    override lateinit var presenter: DetailContract.Presenter

    @Inject
    lateinit var useWebLinkSetting: UseWebLinkSetting

    @Inject
    lateinit var analytics: EventAnalytics

    private val listAdapter by lazy {
        DetailListAdapter(object : DetailListItemListener {

            override fun onInfoClick(item: ListItem) {
                val ctx: Context = this@DetailActivity
                when (item.type) {
                    TYPE_CGV -> {
                        analytics.clickCgvInfo(item.movie)
                        Cgv.executeMobileWeb(ctx, item.movie)
                    }
                    TYPE_LOTTE -> {
                        analytics.clickLotteInfo(item.movie)
                        LotteCinema.executeMobileWeb(ctx, item.movie)
                    }
                    TYPE_MEGABOX -> {
                        analytics.clickMegaboxInfo(item.movie)
                        Megabox.executeMobileWeb(ctx, item.movie)
                    }
                    TYPE_NONE -> {
                        Naver.executeWeb(ctx, item.movie)
                    }
                }
            }

            override fun onTicketClick(item: ListItem) {
                val ctx: Context = this@DetailActivity
                if (useWebLinkSetting.get()) {
                    when (item.type) {
                        TYPE_CGV -> {
                            analytics.clickCgvTicket(item.movie)
                            Cgv.executeWebForSchedule(ctx, item.movie)
                        }
                        TYPE_LOTTE -> {
                            analytics.clickLotteTicket(item.movie)
                            LotteCinema.executeWebForSchedule(ctx, item.movie)
                        }
                        TYPE_MEGABOX -> {
                            analytics.clickMegaboxTicket(item.movie)
                            Megabox.executeWebForSchedule(ctx, item.movie)
                        }
                    }
                } else {
                    when (item.type) {
                        TYPE_CGV -> {
                            analytics.clickCgvTicket(item.movie)
                            Cgv.executeAppForSchedule(ctx)
                        }
                        TYPE_LOTTE -> {
                            analytics.clickLotteTicket(item.movie)
                            LotteCinema.executeAppForSchedule(ctx)
                        }
                        TYPE_MEGABOX -> {
                            analytics.clickMegaboxTicket(item.movie)
                            Megabox.executeAppForSchedule(ctx)
                        }
                    }
                }
            }

            override fun onMoreTrailersClick(item: Movie) {
                analytics.clickMoreTrailers("${item.title} 예고편")
                YouTube.executeAppWithQuery(this@DetailActivity, item)
            }
        }, analytics)
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {

        private var wasScrolled: Boolean = false

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val offset: Int = recyclerView.computeVerticalScrollOffset()
            detailHeaderView.translationZ = max(3f, offset / 800f)

            val isScrolled: Boolean = offset != 0
            if (wasScrolled != isScrolled) {
                wasScrolled = isScrolled
                if (isScrolled) {
                    detailHeaderView.setBackgroundColor(windowBackground)
                } else {
                    detailHeaderView.setBackgroundColorResource(android.R.color.transparent)
                }
            }
        }
    }

    private val shotLoadListener = object : RequestListener<Drawable> {
        override fun onResourceReady(
            resource: Drawable,
            model: Any,
            target: Target<Drawable>,
            dataSource: DataSource,
            isFirstResource: Boolean
        ): Boolean {
            applyTheme(windowBackground)
            doStartPostponedEnterTransition()
            return false
        }

        override fun onLoadFailed(e: GlideException?, model: Any,
                                  target: Target<Drawable>, isFirstResource: Boolean): Boolean {
            doStartPostponedEnterTransition()
            return false
        }
    }

    private lateinit var chromeFader: SystemChromeFader

    override fun onCreate(savedInstanceState: Bundle?) {
        movie = MovieSelectManager.getSelectedItem()!!
        Timber.d("onCreate: movie=%s", movie)
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        binding.item = movie

        chromeFader = object : SystemChromeFader(this) {
            override fun onDragDismissed() = setResultAndFinish()
        }
    }

    private fun doStartPostponedEnterTransition() {
        startPostponedEnterTransition()
    }

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)

        windowBackground = getColorAttr(R.attr.moop_bgColor)

        posterView.loadAsync(movie.posterUrl, shotLoadListener)
        posterView.setOnDebounceClickListener {
            analytics.clickPoster(movie)
            presenter.requestShareImage(movie.posterUrl)
        }
        timetableButton.setOnDebounceClickListener {
            analytics.clickTimetable(movie)
            val intent = Intent(this, TimetableActivity::class.java)
            startActivity(intent, ActivityOptions
                    .makeSceneTransitionAnimation(this, *createSharedElements())
                    .toBundle())
        }
        shareButton.setOnDebounceClickListener {
            analytics.clickShare(movie)
            share(movie)
        }

        listView.apply {
            //TODO: VERY VERY VERY ugly. Please refactor this
            layoutManager = GridLayoutManager(this@DetailActivity, 3).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int =
                            when (listAdapter.getItemViewType(position)) {
                                R.layout.item_detail_trailers -> 3
                                R.layout.item_detail_naver -> 3
                                else -> 1
                            }
                }
            }
            adapter = listAdapter
            itemAnimator = FadeInUpAnimator().apply {
                addDuration = 200
                removeDuration = 200
            }
        }
    }

    private fun createSharedElements(): Array<Pair<View, String>> = arrayOf(
            ageBgView with R.string.transition_age_bg,
            ageView with R.string.transition_age)

    override fun onResume() {
        super.onResume()
        draggableFrame.addListener(chromeFader)
        listView.addOnScrollListener(scrollListener)
    }

    override fun onPause() {
        draggableFrame.removeListener(chromeFader)
        listView.removeOnScrollListener(scrollListener)
        super.onPause()
    }

    override fun render(viewState: DetailViewState) {
        printRenderLog { viewState }
        when (viewState) {
            is LoadingState -> {
                //TODO: show loading state
            }
            is DoneState -> {
                listAdapter.submitList(viewState.items)
            }
        }
    }

    override fun onBackPressed() {
        setResultAndFinish()
    }

    private fun applyTheme(@ColorInt themeBgColor: Int) {
        val isDark: Boolean = ColorUtils.isDark(themeBgColor)
        if (isDark.not()) { // make back icon dark on light images
            val darkColor = getColorAttr(R.attr.moop_iconColorDark)
            titleView.setTextColor(darkColor)
            openDateView.setTextColor(darkColor)
            timetableButton.setColorFilter(darkColor)
            shareButton.setColorFilter(darkColor)

            // set a light status bar
            ViewUtils.setLightStatusBar(window.decorView)
        }

        if (themeBgColor != window.statusBarColor) {
            backgroundView.setBackgroundColor(themeBgColor)
            ValueAnimator.ofArgb(window.statusBarColor, themeBgColor).apply {
                addUpdateListener { animation ->
                    window.statusBarColor = animation.animatedValue as Int
                }
                duration = 500L
                interpolator = getFastOutSlowInInterpolator(this@DetailActivity)
            }.start()
        }
    }

    private fun setResultAndFinish() {
        detailHeaderView.setBackgroundColorResource(android.R.color.transparent)
        finishAfterTransition()
    }

    override fun doShareImage(imageUri: Uri, mimeType: String) {
        ShareCompat.IntentBuilder.from(this)
                .setChooserTitle(R.string.action_share_poster)
                .setSubject(movie.title)
                .setStream(imageUri)
                .setType(mimeType)
                .startChooser()
    }
}
