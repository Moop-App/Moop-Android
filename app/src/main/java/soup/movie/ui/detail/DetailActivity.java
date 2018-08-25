package soup.movie.ui.detail;

import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;
import soup.movie.R;
import soup.movie.data.model.Movie;
import soup.movie.data.model.TimeTable;
import soup.movie.data.model.Trailer;
import soup.movie.ui.BaseActivity;
import soup.movie.ui.detail.DetailViewState.DoneState;
import soup.movie.ui.detail.DetailViewState.LoadingState;
import soup.movie.util.DrawableUtils;
import soup.movie.util.ImageUtil;
import soup.movie.util.MovieUtil;
import soup.widget.elastic.ElasticDragDismissFrameLayout;
import soup.widget.util.ColorUtils;
import soup.widget.util.ViewUtils;
import timber.log.Timber;

import static soup.movie.util.IntentUtil.createShareIntentWithText;
import static soup.movie.util.RecyclerViewUtil.createLinearLayoutManager;
import static soup.widget.util.AnimUtils.getFastOutSlowInInterpolator;

public class DetailActivity extends BaseActivity implements DetailContract.View {

    private static final float SCRIM_ADJUSTMENT = 0.075f;

    @BindView(R.id.draggable_frame)
    ElasticDragDismissFrameLayout draggableFrame;

    @BindView(R.id.background)
    View background;

    @BindView(R.id.back)
    ImageView backButton;

    @BindView(R.id.movie_poster)
    ImageView posterView;

    @BindView(R.id.primary_text)
    TextView titleView;

    @BindView(R.id.sub_text1)
    TextView ageView;

    @BindView(R.id.age_bg)
    View ageBgView;

    @BindView(R.id.sub_text2)
    TextView eggView;

    @BindView(R.id.favorite_button)
    ImageView favoriteButton;

    @BindView(R.id.share_button)
    ImageView shareButton;

    @BindView(R.id.movie_contents)
    RecyclerView movieContents;

    @BindColor(R.color.green)
    int greenColor;

    @BindColor(R.color.blue)
    int blueColor;

    @BindColor(R.color.amber)
    int amberColor;

    @BindColor(R.color.red)
    int redColor;

    @BindColor(R.color.grey)
    int greyColor;

    @Inject
    DetailContract.Presenter presenter;

    private DetailListAdapter adapterView;

    private ElasticDragDismissFrameLayout.SystemChromeFader chromeFader;

    private Movie movie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            movie = MovieUtil.restoreFrom(getIntent());
        } else {
            movie = MovieUtil.restoreFrom(savedInstanceState);
        }
        Timber.d("onCreate: movie=%s", movie);

        ImageUtil.loadAsync(this, posterView, shotLoadListener, movie.getPoster());
        titleView.setText(movie.getTitle());
        updateAgeView(movie.getAge());
        eggView.setText(movie.getEgg());
        favoriteButton.setOnClickListener(v -> {});
        shareButton.setOnClickListener(v ->
            startActivity(createShareIntentWithText(
                    "공유하기", MovieUtil.createShareDescription(movie))));

        backButton.setOnClickListener(v -> setResultAndFinish());
        chromeFader = new ElasticDragDismissFrameLayout.SystemChromeFader(this) {
            @Override
            public void onDragDismissed() {
                setResultAndFinish();
            }
        };

        DetailListAdapter adapterView = new DetailListAdapter(this);
        RecyclerView recyclerView = movieContents;
        recyclerView.setLayoutManager(createLinearLayoutManager(this, true));
        recyclerView.setAdapter(adapterView);
        recyclerView.setItemAnimator(new SlideInRightAnimator());
        recyclerView.getItemAnimator().setAddDuration(200);
        recyclerView.getItemAnimator().setRemoveDuration(200);
        this.adapterView = adapterView;

        presenter.attach(this);

        //TODO: call requestData() after transition animation is ended
        register(Observable.timer(150, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(unused -> presenter.requestData(movie)));
    }

    private void updateAgeView(String ageText) {
        int color;
        switch (ageText) {
            case "전체 관람가":
                ageText = "전체";
                color = greenColor;
                break;
            case "12세 관람가":
                ageText = "12";
                color = blueColor;
                break;
            case "15세 관람가":
                ageText = "15";
                color = amberColor;
                break;
            case "청소년관람불가":
                ageText = "청불";
                color = redColor;
                break;
            default:
                ageText = "미정";
                color = greyColor;
        }
        ageView.setText(ageText);
        ageBgView.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    @Override
    protected void onResume() {
        super.onResume();
        draggableFrame.addListener(chromeFader);
    }

    @Override
    protected void onPause() {
        draggableFrame.removeListener(chromeFader);
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MovieUtil.saveTo(outState, movie);
    }

    @Override
    protected void onDestroy() {
        presenter = null;
        super.onDestroy();
    }

    @Override
    public void render(@NonNull DetailViewState viewState) {
        if (viewState instanceof LoadingState) {
            renderInternal((LoadingState) viewState);
        } else if (viewState instanceof DoneState) {
            renderInternal((DoneState) viewState);
        }
    }

    private void renderInternal(@NonNull LoadingState viewState) {
        //TODO: show loading state
    }

    private void renderInternal(@NonNull DoneState viewState) {
        updateTrailerList(viewState.getTimeTable(), viewState.getTrailers());
    }

    private void updateTrailerList(@Nullable TimeTable timeTable, @Nullable List<Trailer> trailerList) {
        DetailListAdapter adapterView = this.adapterView;
        if (adapterView != null) {
            adapterView.updateList(timeTable, trailerList);
        }
    }

    @Override
    public void onBackPressed() {
        setResultAndFinish();
    }

    @Override
    public boolean onNavigateUp() {
        setResultAndFinish();
        return true;
    }

    void setResultAndFinish() {
        finishAfterTransition();
    }

    private RequestListener<Drawable> shotLoadListener = new RequestListener<Drawable>() {
        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                       DataSource dataSource, boolean isFirstResource) {
            final Bitmap bitmap = DrawableUtils.getBitmap(resource);
            if (bitmap == null) return false;
            final int twentyFourDip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    24, DetailActivity.this.getResources().getDisplayMetrics());
            Palette.from(bitmap)
                    .maximumColorCount(3)
                    .clearFilters() /* by default palette ignore certain hues
                        (e.g. pure black/white) but we don't want this. */
                    .setRegion(0, 0, bitmap.getWidth() - 1, twentyFourDip) /* - 1 to work around
                        https://code.google.com/p/android/issues/detail?id=191013 */
                    .generate(palette -> {
                        boolean isDark;
                        @ColorUtils.Lightness int lightness = ColorUtils.isDark(palette);
                        if (lightness == ColorUtils.LIGHTNESS_UNKNOWN) {
                            isDark = ColorUtils.isDark(bitmap, bitmap.getWidth() / 2, 0);
                        } else {
                            isDark = lightness == ColorUtils.IS_DARK;
                        }

                        int adaptiveColor = ContextCompat.getColor(DetailActivity.this,
                                isDark ? R.color.white : R.color.dark_icon);
                        backButton.setColorFilter(adaptiveColor);
                        titleView.setTextColor(adaptiveColor);
                        eggView.setTextColor(adaptiveColor);
                        favoriteButton.setColorFilter(adaptiveColor);
                        shareButton.setColorFilter(adaptiveColor);

                        // color the status bar. Set a complementary dark color on L,
                        // light or dark color on M (with matching status bar icons)
                        int statusBarColor = getWindow().getStatusBarColor();
                        final Palette.Swatch topColor =
                                ColorUtils.getMostPopulousSwatch(palette);
                        if (topColor != null &&
                                (isDark || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                            statusBarColor = ColorUtils.scrimify(topColor.getRgb(),
                                    isDark, SCRIM_ADJUSTMENT);
                            // set a light status bar on M+
                            if (!isDark) {
                                ViewUtils.setLightStatusBar(posterView);
                            }
                        }

                        if (statusBarColor != getWindow().getStatusBarColor()) {
                            background.setBackgroundColor(statusBarColor);
                            ValueAnimator statusBarColorAnim = ValueAnimator.ofArgb(
                                    getWindow().getStatusBarColor(), statusBarColor);
                            statusBarColorAnim.addUpdateListener(animation -> getWindow().setStatusBarColor(
                                    (int) animation.getAnimatedValue()));
                            statusBarColorAnim.setDuration(500L);
                            statusBarColorAnim.setInterpolator(
                                    getFastOutSlowInInterpolator(DetailActivity.this));
                            statusBarColorAnim.start();
                        }
                    });
            return false;
        }

        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                    Target<Drawable> target, boolean isFirstResource) {
            return false;
        }
    };
}
