package soup.movie.ui.main.settings;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import soup.movie.settings.HomeTypeSetting;
import soup.movie.settings.TheaterSetting;
import soup.movie.ui.BasePresenter;
import soup.movie.ui.main.settings.SettingsViewState.DoneState;
import timber.log.Timber;

public class SettingsPresenter extends BasePresenter<SettingsContract.View>
        implements SettingsContract.Presenter {

    private final HomeTypeSetting homeTypeSetting;
    private final TheaterSetting theaterSetting;

    @Inject
    SettingsPresenter(HomeTypeSetting homeTypeSetting,
                      TheaterSetting theaterSetting) {
        this.homeTypeSetting = homeTypeSetting;
        this.theaterSetting = theaterSetting;
    }

    @Override
    protected void initObservable(CompositeDisposable subscriptions) {
        subscriptions.add(getViewStateObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::render, Timber::e));
    }

    private Single<SettingsViewState> getViewStateObservable() {
        return Single.zip(
                Single.fromCallable(homeTypeSetting::isVerticalType),
                Single.fromCallable(theaterSetting::getFavoriteTheaters),
                DoneState::new);
    }
}
