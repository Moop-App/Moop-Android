package soup.movie.ui.home;

import android.support.annotation.NonNull;

import java.util.List;

import soup.movie.data.soup.model.Movie;
import soup.movie.ui.BaseUiModel;

interface HomeUiModel extends BaseUiModel {

    class InProgress implements HomeUiModel {

        InProgress() {
        }

        @Override
        public String toString() {
            return "HomeUiModel.InProgress{}";
        }
    }

    class Data implements HomeUiModel {

        private final List<Movie> mMovies;

        Data(@NonNull List<Movie> movies) {
            mMovies = movies;
        }

        @NonNull
        List<Movie> getMovies() {
            return mMovies;
        }

        @Override
        public String toString() {
            return "HomeUiModel.Data{" +
                    "movies=" + mMovies +
                    '}';
        }
    }
}
