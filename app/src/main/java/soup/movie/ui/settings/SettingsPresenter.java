package soup.movie.ui.settings;

public class SettingsPresenter implements SettingsContract.Presenter {

    private SettingsContract.View mView;

    SettingsPresenter() {
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
