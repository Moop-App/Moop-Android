package soup.movie.ui.detail;

import android.support.annotation.NonNull;

import soup.movie.data.soup.model.Movie;
import soup.movie.ui.BaseContract;

class DetailContract {

    interface Presenter extends BaseContract.Presenter<View> {
        void requestData(@NonNull Movie movie);
        void requestData(@NonNull String code, @NonNull Movie movie);
    }

    interface View extends BaseContract.View {
        void render(@NonNull DetailUiModel uiModel);
    }
}
