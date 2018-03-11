package soup.movie.ui.boxoffice;

import soup.movie.ui.BaseContract;

public interface BoxOfficeContract {

    interface Presenter extends BaseContract.Presenter<View> {
        void refresh();
    }

    interface View extends BaseContract.View {
        void render(BoxOfficeUiModel uiModel);
    }
}
