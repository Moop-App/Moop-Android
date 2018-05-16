package soup.movie.ui.main.settings;

import android.support.annotation.NonNull;

import soup.movie.ui.BaseContract;

public interface SettingsContract {

    interface Presenter extends BaseContract.Presenter<View> {
        void onVerticalHomeTypeClicked();
        void onHorizontalHomeTypeClicked();
    }

    interface View extends BaseContract.View {
        void render(@NonNull SettingsViewState viewState);
    }
}
