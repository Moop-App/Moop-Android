package soup.movie.ui.archive;

import soup.movie.ui.BaseContract;

interface ArchiveContract {

    interface Presenter extends BaseContract.Presenter<View> {
        void refresh();
    }

    interface View extends BaseContract.View {
        void render(ArchiveUiModel uiModel);
    }
}
