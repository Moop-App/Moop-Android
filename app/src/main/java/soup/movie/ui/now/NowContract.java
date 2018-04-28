package soup.movie.ui.now;

import android.support.annotation.NonNull;

import soup.movie.ui.BaseContract;

public interface NowContract {

    interface Presenter extends BaseContract.Presenter<View> {
    }

    interface View extends BaseContract.View {
        void render(@NonNull NowViewState viewState);
    }
}
