package soup.movie.ui.main.settings;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import soup.movie.ui.main.settings.SettingsViewState.DoneState;
import soup.movie.util.TheaterUtil;
import soup.movie.util.DialogUtil;

public class SettingsPresenter implements SettingsContract.Presenter {

    private SettingsContract.View view;

    @Inject
    SettingsPresenter() {
    }

    @Override
    public void attach(SettingsContract.View view) {
        this.view = view;
        this.view.render(new DoneState(TheaterUtil.getMyTheaterList()));
    }

    @Override
    public void detach() {
        view = null;
    }

    @Override
    public void onClick(@NonNull Context context) {
        DialogUtil.startDialogToSelectTheaters(context,
                asyncData -> view.render(new DoneState(asyncData)));
    }
}
