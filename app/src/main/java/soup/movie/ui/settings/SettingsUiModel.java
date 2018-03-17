package soup.movie.ui.settings;

import android.support.annotation.NonNull;

import java.util.List;

import soup.movie.data.kobis.model.Movie;
import soup.movie.ui.BaseUiModel;

interface SettingsUiModel extends BaseUiModel {

    class InProgress implements SettingsUiModel {

        InProgress() {
        }

        @Override
        public String toString() {
            return "SettingsUiModel.InProgress{}";
        }
    }

    class Data implements SettingsUiModel {

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
            return "SettingsUiModel.Data{" +
                    "movies=" + mMovies +
                    '}';
        }
    }
}
