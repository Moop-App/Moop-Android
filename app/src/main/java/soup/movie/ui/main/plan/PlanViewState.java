package soup.movie.ui.main.plan;

import android.support.annotation.NonNull;

import java.util.List;

import soup.movie.data.model.Movie;
import soup.movie.ui.BaseViewState;

interface PlanViewState extends BaseViewState {

    class LoadingState implements PlanViewState {

        LoadingState() {
        }

        @Override
        public String toString() {
            return "LoadingState{}";
        }
    }

    class DoneState implements PlanViewState {

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
