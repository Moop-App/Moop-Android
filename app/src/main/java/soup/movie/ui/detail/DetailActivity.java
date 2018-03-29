package soup.movie.ui.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import soup.movie.R;
import soup.movie.data.soup.model.Movie;
import soup.movie.data.utils.MovieUtil;
import soup.movie.ui.util.ImageUtil;
import timber.log.Timber;

public class DetailActivity extends AppCompatActivity {

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

        ImageUtil.loadAsync(this, mPosterView, movie.getThumbnailUrl());
        mTitleView.setText(movie.getTitle());
        mAgeView.setText(movie.getAge());
        mEggView.setText(movie.getEgg());
        mFavoriteButton.setOnClickListener(v -> {});
        mShareButton.setOnClickListener(v -> {});
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MovieUtil.saveTo(outState, movie);
    }
}
