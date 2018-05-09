package soup.movie.ui.theater.sort;

import android.support.annotation.NonNull;

import java.util.List;

import soup.movie.data.model.TheaterCode;
import soup.movie.ui.BaseContract;

class TheaterSortContract {

    interface Presenter extends BaseContract.Presenter<View> {
        void onConfirmClicked(List<TheaterCode> selectedTheaters);
    }

    interface View extends BaseContract.View {
        void render(@NonNull TheaterSortViewState viewState);
    }
}
