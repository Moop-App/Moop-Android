package soup.movie.ui.main;

import static soup.movie.ui.main.MainContract.TAB_MODE_BOX_OFFICE;
import static soup.movie.ui.main.MainContract.TAB_MODE_HOME;
import static soup.movie.ui.main.MainContract.TAB_MODE_SETTINGS;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;

    @Override
    public void attach(MainContract.View view) {
        mView = view;
    }

    @Override
    public void detach() {
        mView = null;
    }

    @Override
    public void setTabMode(@MainContract.TabMode int mode) {
        MainUiModel uiModel;
        switch (mode) {
            case TAB_MODE_BOX_OFFICE:
                uiModel = new MainUiModel.BoxOffice();
                break;
            case TAB_MODE_HOME:
                uiModel = new MainUiModel.Home();
                break;
            case TAB_MODE_SETTINGS:
                uiModel = new MainUiModel.Settings();
                break;
            default:
                throw new IllegalStateException("Unknown tab mode");
        }
        mView.render(uiModel);
    }
}
