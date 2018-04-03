package soup.movie.ui.settings;

import android.content.Context;
import android.support.annotation.NonNull;

import soup.movie.data.utils.TheaterUtil;
import soup.movie.ui.util.DialogUtil;

public class SettingsPresenter implements SettingsContract.Presenter {

    private SettingsContract.View mView;

    SettingsPresenter() {
    }

    @Override
    public void attach(SettingsContract.View view) {
        mView = view;
        mView.render(new SettingsUiModel.Data(TheaterUtil.getMyTheaterList()));
    }

    @Override
    public void detach() {
        mView = null;
    }

    @Override
    public void onClick(@NonNull Context context) {
        DialogUtil.startDialogToSelectTheaters(context,
                asyncData -> mView.render(new SettingsUiModel.Data(asyncData)));
    }
}
