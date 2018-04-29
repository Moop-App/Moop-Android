package soup.movie.ui.main.now;

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

        private final List<Movie> movies;

        DoneState(@NonNull List<Movie> movies) {
            this.movies = movies;
        }

        @NonNull
        public List<Movie> getMovies() {
            return movies;
        }

        @Override
        public String toString() {
            return "DoneState{" +
                    "movies=" + movies +
                    '}';
        }
    }
}
