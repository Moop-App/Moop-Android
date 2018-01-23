package soup.movie.ui.preview;

import soup.movie.data.Movie;
import java.util.List;

class MoviePreviewContract {

    interface Presenter {
        void bind();
        void unbind();

        void refresh();
        void loadItems();
    }

    interface View {
        void onListUpdated(List<Movie> items);
        void onRefreshDone();
    }

    interface AdapterView {
        void updateList(List<Movie> items);
    }
}
