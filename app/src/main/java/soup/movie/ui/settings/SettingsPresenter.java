package soup.movie.ui.settings;

import soup.movie.Injection;

public class SettingsPresenter implements SettingsContract.Presenter {

    private Injection mInjection;
    private SettingsContract.View mView;

    SettingsPresenter() {
        mInjection = new Injection();
    }

    @Override
    public void attach(SettingsContract.View view) {
        mView = view;
    }

    @Override
    public void detach() {
        mView = null;
    }
}
