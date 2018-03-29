package soup.movie.ui.detail;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;
import soup.movie.R;
import soup.movie.data.soup.model.Movie;
import soup.movie.data.soup.model.Trailer;
import soup.movie.data.utils.MovieUtil;
import soup.movie.ui.util.ImageUtil;
import soup.movie.ui.widget.ElasticDragDismissFrameLayout;
import soup.movie.util.ColorUtils;
import soup.movie.util.ViewUtils;
import soup.movie.util.glide.GlideUtils;
import timber.log.Timber;

import static soup.movie.ui.util.RecyclerViewUtil.createLinearLayoutManager;
import static soup.movie.util.AnimUtils.getFastOutSlowInInterpolator;

public class DetailActivity extends AppCompatActivity implements DetailContract.View {

    private static final float SCRIM_ADJUSTMENT = 0.075f;

    @BindView(R.id.draggable_frame)
    ElasticDragDismissFrameLayout draggableFrame;

    @BindView(R.id.background)
    View mBackground;

    @BindView(R.id.back)
    ImageView mBackButton;

    @BindView(R.id.movie_poster)
    ImageView mPosterView;

    @BindView(R.id.primary_text)
    TextView mTitleView;

    @BindView(R.id.sub_text1)
    TextView mAgeView;

    @BindView(R.id.sub_text2)
    TextView mEggView;

    @BindView(R.id.favorite_button)
    ImageView mFavoriteButton;

    @BindView(R.id.share_button)
    ImageView mShareButton;

    @BindView(R.id.movie_contents)
    RecyclerView mMovieContents;

    private Movie movie;

    private DetailContract.Presenter mPresenter;
    private DetailListAdapter mAdapterView;

    private ElasticDragDismissFrameLayout.SystemChromeFader mChromeFader;

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

        ImageUtil.loadAsync(this, mPosterView, shotLoadListener, movie.getPosterUrl());
        mTitleView.setText(movie.getTitle());
        mAgeView.setText(movie.getAge());
        mEggView.setText(movie.getEgg());
        mFavoriteButton.setOnClickListener(v -> {});
        mShareButton.setOnClickListener(v -> {});


        mBackButton.setOnClickListener(v -> setResultAndFinish());
        mChromeFader = new ElasticDragDismissFrameLayout.SystemChromeFader(this) {
            @Override
            public void onDragDismissed() {
                setResultAndFinish();
            }
        };

        DetailListAdapter adapterView = new DetailListAdapter(this);
        RecyclerView recyclerView = mMovieContents;
        recyclerView.setLayoutManager(createLinearLayoutManager(this, true));
        recyclerView.setAdapter(adapterView);
        recyclerView.setItemAnimator(new SlideInRightAnimator());
        recyclerView.getItemAnimator().setAddDuration(200);
        recyclerView.getItemAnimator().setRemoveDuration(200);
        mAdapterView = adapterView;

        mPresenter = new DetailPresenter();
        mPresenter.attach(this);
        mPresenter.requestData(movie.getId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        draggableFrame.addListener(mChromeFader);
    }

    @Override
    protected void onPause() {
        draggableFrame.removeListener(mChromeFader);
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MovieUtil.saveTo(outState, movie);
    }

    @Override
    protected void onDestroy() {
        mPresenter = null;
        super.onDestroy();
    }

    @Override
    public void render(@NonNull DetailUiModel uiModel) {
        if (uiModel instanceof DetailUiModel.Loading) {
            //TODO: show loading state
        } else if (uiModel instanceof DetailUiModel.Done) {
            List<Trailer> trailers = ((DetailUiModel.Done) uiModel).getTrailers();
            updateTrailerList(trailers);
        }
    }

    private void updateTrailerList(@Nullable List<Trailer> trailerList) {
        DetailListAdapter adapterView = mAdapterView;
        if (adapterView != null) {
            adapterView.updateList(trailerList);
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
            final Bitmap bitmap = GlideUtils.getBitmap(resource);
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
                        mBackButton.setColorFilter(adaptiveColor);
                        mTitleView.setTextColor(adaptiveColor);
                        mAgeView.setTextColor(adaptiveColor);
                        mEggView.setTextColor(adaptiveColor);
                        mFavoriteButton.setColorFilter(adaptiveColor);
                        mShareButton.setColorFilter(adaptiveColor);

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
                                ViewUtils.setLightStatusBar(mPosterView);
                            }
                        }

                        if (statusBarColor != getWindow().getStatusBarColor()) {
                            mBackground.setBackgroundColor(statusBarColor);
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
