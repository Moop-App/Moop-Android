package soup.movie.ui.detail;

import java.util.List;

import soup.movie.data.model.TimeTable;
import soup.movie.data.model.Trailer;
import soup.movie.ui.BaseViewState;

interface DetailViewState extends BaseViewState {

    class LoadingState implements DetailViewState {

        private boolean mTheaterNotExists;

        LoadingState() {
            this(true);
        }

        LoadingState(boolean theaterExists) {
            mTheaterNotExists = theaterExists;
        }

        public boolean isTheaterNotExists() {
            return mTheaterNotExists;
        }

        @Override
        public String toString() {
            return "LoadingState{}";
        }
    }

    class DoneState implements DetailViewState {

        private TimeTable timeTable;
        private List<Trailer> trailers;

        DoneState(TimeTable timeTable, List<Trailer> trailers) {
            this.timeTable = timeTable;
            this.trailers = trailers;
        }

        public TimeTable getTimeTable() {
            return timeTable;
        }

        public List<Trailer> getTrailers() {
            return trailers;
        }

        @Override
        public String toString() {
            return "DoneState{" +
                    "timeTable=" + timeTable +
                    ", trailers=" + trailers +
                    '}';
        }
    }
}
