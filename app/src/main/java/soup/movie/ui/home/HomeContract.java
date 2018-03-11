package soup.movie.ui.home;

import soup.movie.ui.BaseContract;

public interface HomeContract {

    interface Presenter extends BaseContract.Presenter<View> {
        void refresh();
    }

    interface View extends BaseContract.View {
        void render(HomeUiModel uiModel);
    }
}
