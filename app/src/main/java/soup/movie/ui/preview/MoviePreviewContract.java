package soup.movie.ui.preview;

import soup.movie.data.Movie;
import java.util.List;

class MoviePreviewContract {

    interface Presenter {
        void bind();
        void unbind();

        void loadItems();
    }

    interface View {
        void showList(List<Movie> items);
    }

    interface AdapterView {
        void updateList(List<Movie> items);
    }
}
