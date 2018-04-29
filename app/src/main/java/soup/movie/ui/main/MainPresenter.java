package soup.movie.ui.main;

import javax.inject.Inject;

import soup.movie.di.ActivityScoped;
import soup.movie.ui.BasePresenter;

import static soup.movie.ui.main.MainContract.TAB_MODE_NOW;
import static soup.movie.ui.main.MainContract.TAB_MODE_PLAN;
import static soup.movie.ui.main.MainContract.TAB_MODE_SETTINGS;

@ActivityScoped
public class MainPresenter extends BasePresenter<MainContract.View>
        implements MainContract.Presenter {

    @Inject
    MainPresenter() {
    }

    @Override
    public void setTabMode(@MainContract.TabMode int mode) {
        MainViewState viewState;
        switch (mode) {
            case TAB_MODE_NOW:
                viewState = new MainViewState.NowState();
                break;
            case TAB_MODE_PLAN:
                viewState = new MainViewState.PlanState();
                break;
            case TAB_MODE_SETTINGS:
                viewState = new MainViewState.SettingsState();
                break;
            default:
                throw new IllegalStateException("Unknown tab mode");
        }
        view.render(viewState);
    }
}
