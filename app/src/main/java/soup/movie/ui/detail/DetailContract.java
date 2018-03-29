package soup.movie.ui.detail;

import android.support.annotation.NonNull;

import soup.movie.ui.BaseContract;

class DetailContract {

    interface Presenter extends BaseContract.Presenter<View> {
        void requestData(@NonNull String movieId);
    }

    interface View extends BaseContract.View {
        void render(@NonNull DetailUiModel uiModel);
    }
}
