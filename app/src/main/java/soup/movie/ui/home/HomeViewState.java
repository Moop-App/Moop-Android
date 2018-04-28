package soup.movie.ui.home;

import android.support.annotation.NonNull;

import java.util.List;

import soup.movie.data.soup.model.Movie;
import soup.movie.ui.BaseViewState;

interface HomeViewState extends BaseViewState {

    class InProgress implements HomeViewState {

        InProgress() {
        }

        @Override
        public String toString() {
            return "InProgress{}";
        }
    }

    class Data implements HomeViewState {

        private final String mTitle;
        private final List<Movie> mMovies;

        Data(@NonNull String title, @NonNull List<Movie> movies) {
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
            return "Data{" +
                    "mTitle='" + mTitle + '\'' +
                    ", mMovies=" + mMovies +
                    '}';
        }
    }
}
