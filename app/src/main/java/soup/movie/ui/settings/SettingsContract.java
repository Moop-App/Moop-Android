package soup.movie.ui.settings;

import soup.movie.ui.BaseContract;

public interface SettingsContract {

    interface Presenter extends BaseContract.Presenter<View> {
    }

    interface View extends BaseContract.View {
        void render(SettingsUiModel uiModel);
    }
}
