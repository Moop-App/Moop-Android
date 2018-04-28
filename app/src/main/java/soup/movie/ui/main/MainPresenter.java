package soup.movie.ui.main;

import static soup.movie.ui.main.MainContract.TAB_MODE_NOW;
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
        MainViewState uiModel;
        switch (mode) {
            case TAB_MODE_NOW:
                uiModel = new MainViewState.Home();
                break;
            case TAB_MODE_SETTINGS:
                uiModel = new MainViewState.Settings();
                break;
            default:
                throw new IllegalStateException("Unknown tab mode");
        }
        mView.render(uiModel);
    }
}
