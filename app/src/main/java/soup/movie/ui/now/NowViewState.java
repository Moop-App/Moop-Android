package soup.movie.ui.now;

import android.support.annotation.NonNull;

import java.util.List;

import soup.movie.data.soup.model.Movie;
import soup.movie.ui.BaseViewState;

interface NowViewState extends BaseViewState {

    class LoadingState implements NowViewState {

        LoadingState() {
        }

        @Override
        public String toString() {
            return "LoadingState{}";
        }
    }

    class DoneState implements NowViewState {

        private final String mTitle;
        private final List<Movie> mMovies;

        DoneState(@NonNull String title, @NonNull List<Movie> movies) {
            mTitle = title;
            mMovies = movies;
        }

        @NonNull
        public String getTitle() {
            return mTitle;
        }

        @NonNull
        public List<Movie> getMovies() {
            return mMovies;
        }

        @Override
        public String toString() {
            return "DoneState{" +
                    "mTitle='" + mTitle + '\'' +
                    ", mMovies=" + mMovies +
                    '}';
        }
    }
}
