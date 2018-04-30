package soup.movie.ui.main.now;

import android.support.annotation.NonNull;

import soup.movie.ui.BaseContract;

public interface NowContract {

    interface Presenter extends BaseContract.Presenter<View> {
        void refresh();
    }

    interface View extends BaseContract.View {
        void render(@NonNull NowViewState viewState);
    }
}
