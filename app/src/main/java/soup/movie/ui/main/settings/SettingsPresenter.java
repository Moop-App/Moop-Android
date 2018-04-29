package soup.movie.ui.main.settings;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import soup.movie.settings.TheaterSetting;
import soup.movie.ui.BasePresenter;
import soup.movie.ui.main.settings.SettingsViewState.DoneState;
import timber.log.Timber;

public class SettingsPresenter extends BasePresenter<SettingsContract.View>
        implements SettingsContract.Presenter {

    private final TheaterSetting theaterSetting;

    @Inject
    SettingsPresenter(TheaterSetting theaterSetting) {
        this.theaterSetting = theaterSetting;
    }

    @Override
    protected void initObservable(CompositeDisposable subscriptions) {
        subscriptions.add(getViewStateObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::render, Timber::e));
    }

    private Single<SettingsViewState> getViewStateObservable() {
        return Single.fromCallable(theaterSetting::getFavoriteTheaters)
                .map(DoneState::new);
    }
}
