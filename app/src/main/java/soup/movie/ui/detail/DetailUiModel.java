package soup.movie.ui.detail;

import java.util.List;

import soup.movie.data.soup.model.Trailer;
import soup.movie.ui.BaseUiModel;

interface DetailUiModel extends BaseUiModel {

    class Loading implements DetailUiModel {

        Loading() {
        }

        @Override
        public String toString() {
            return "Loading{}";
        }
    }

    class Done implements DetailUiModel {

        private List<Trailer> trailers;

        Done(List<Trailer> trailers) {
            this.trailers = trailers;
        }

        public List<Trailer> getTrailers() {
            return trailers;
        }

        @Override
        public String toString() {
            return "Done{" +
                    "trailers=" + trailers +
                    '}';
        }
    }
}
