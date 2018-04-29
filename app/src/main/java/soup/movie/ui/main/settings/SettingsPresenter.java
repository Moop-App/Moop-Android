package soup.movie.ui.main.settings;

import android.content.Context;
import android.support.annotation.NonNull;

import soup.movie.data.utils.TheaterUtil;
import soup.movie.util.DialogUtil;

public class SettingsPresenter implements SettingsContract.Presenter {

    private SettingsContract.View view;

    SettingsPresenter() {
    }

    @Override
    public void attach(SettingsContract.View view) {
        this.view = view;
        this.view.render(new SettingsViewState.DoneState(TheaterUtil.getMyTheaterList()));
    }

    @Override
    public void detach() {
        view = null;
    }

    @Override
    public void onClick(@NonNull Context context) {
        DialogUtil.startDialogToSelectTheaters(context,
                asyncData -> view.render(new SettingsViewState.DoneState(asyncData)));
    }
}
