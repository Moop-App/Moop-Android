package soup.movie.ui.settings;

import android.content.Context;
import android.support.annotation.NonNull;

import soup.movie.ui.BaseContract;

public interface SettingsContract {

    interface Presenter extends BaseContract.Presenter<View> {
        void onClick(@NonNull Context instantContext);
    }

    interface View extends BaseContract.View {
        void render(SettingsViewState uiModel);
    }
}
