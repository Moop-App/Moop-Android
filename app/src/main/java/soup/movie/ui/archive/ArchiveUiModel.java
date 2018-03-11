package soup.movie.ui.archive;

import android.support.annotation.NonNull;

import java.util.List;

import soup.movie.data.Movie;
import soup.movie.ui.BaseUiModel;

interface ArchiveUiModel extends BaseUiModel {

    class InProgress implements ArchiveUiModel {

        public InProgress() {
        }

        @Override
        public String toString() {
            return "ArchiveUiModel.InProgress{}";
        }
    }

    class Data implements ArchiveUiModel {

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
            return "ArchiveUiModel.Data{" +
                    "movies=" + mMovies +
                    '}';
        }
    }
}
