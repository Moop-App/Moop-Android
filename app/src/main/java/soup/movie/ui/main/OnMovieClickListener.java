package soup.movie.ui.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.View;

import soup.movie.data.model.Movie;

public interface OnMovieClickListener {

    void onMovieClick(@NonNull Movie movie, @Nullable Pair<View, String>... sharedElements);
}
