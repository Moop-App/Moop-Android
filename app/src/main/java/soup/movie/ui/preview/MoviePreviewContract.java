package soup.movie.ui.preview;

import soup.movie.data.kobis.model.Movie;
import soup.movie.ui.BaseContract;

import java.util.List;

class MoviePreviewContract {

    interface Presenter extends BaseContract.Presenter<View> {
        void refresh();
        void loadItems();
    }

    interface View extends BaseContract.View {
        void onClearList();
        void onListUpdated(List<Movie> items);
        void onRefreshDone();
    }

    interface AdapterView {
        void updateList(List<Movie> items);
    }
}
