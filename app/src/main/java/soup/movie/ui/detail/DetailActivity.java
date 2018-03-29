package soup.movie.ui.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

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
import timber.log.Timber;

import static soup.movie.ui.util.RecyclerViewUtil.createLinearLayoutManager;

public class DetailActivity extends AppCompatActivity implements DetailContract.View {

    @BindView(R.id.draggable_frame)
    ElasticDragDismissFrameLayout draggableFrame;

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

    private ElasticDragDismissFrameLayout.SystemChromeFader chromeFader;

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

        ImageUtil.loadAsync(this, mPosterView, movie.getPosterUrl());
        mTitleView.setText(movie.getTitle());
        mAgeView.setText(movie.getAge());
        mEggView.setText(movie.getEgg());
        mFavoriteButton.setOnClickListener(v -> {});
        mShareButton.setOnClickListener(v -> {});

        chromeFader = new ElasticDragDismissFrameLayout.SystemChromeFader(this) {
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
}
