package soup.movie.ui.detail;

import java.util.List;

import soup.movie.data.soup.model.TimeTable;
import soup.movie.data.soup.model.Trailer;
import soup.movie.ui.BaseViewState;

interface DetailViewState extends BaseViewState {

    class Loading implements DetailViewState {

        private boolean mTheaterNotExists;

        Loading() {
            this(true);
        }

        Loading(boolean theaterExists) {
            mTheaterNotExists = theaterExists;
        }

        public boolean isTheaterNotExists() {
            return mTheaterNotExists;
        }

        @Override
        public String toString() {
            return "Loading{}";
        }
    }

    class Data implements DetailViewState {

        private TimeTable timeTable;
        private List<Trailer> trailers;

        Data(TimeTable timeTable, List<Trailer> trailers) {
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
            return "Data{" +
                    "timeTable=" + timeTable +
                    ", trailers=" + trailers +
                    '}';
        }
    }
}
