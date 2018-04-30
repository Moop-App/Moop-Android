package soup.movie.ui.main.settings;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;
import com.jakewharton.rxrelay2.ReplayRelay;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;
import soup.movie.settings.HomeTypeSetting;
import soup.movie.settings.TheaterSetting;
import soup.movie.ui.BasePresenter;
import soup.movie.ui.main.settings.SettingsViewState.DoneState;
import timber.log.Timber;

public class SettingsPresenter extends BasePresenter<SettingsContract.View>
        implements SettingsContract.Presenter {

    private final HomeTypeSetting homeTypeSetting;
    private final TheaterSetting theaterSetting;

    private final BehaviorRelay<Boolean> homeTypeSubject;

    @Inject
    SettingsPresenter(HomeTypeSetting homeTypeSetting,
                      TheaterSetting theaterSetting) {
        this.homeTypeSetting = homeTypeSetting;
        this.theaterSetting = theaterSetting;
        homeTypeSubject = BehaviorRelay.createDefault(homeTypeSetting.isVerticalType());
    }

    @Override
    protected void initObservable(CompositeDisposable subscriptions) {
        subscriptions.add(getViewStateObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::render, Timber::e));
    }

    private Flowable<SettingsViewState> getViewStateObservable() {
        return Flowable.combineLatest(
                getHomeTypeObservable(),
                Flowable.fromCallable(theaterSetting::getFavoriteTheaters),
                DoneState::new);
    }

    private Flowable<Boolean> getHomeTypeObservable() {
        return homeTypeSubject.toFlowable(BackpressureStrategy.LATEST)
                .distinctUntilChanged();
    }

    @Override
    public void onVerticalHomeTypeClicked() {
        homeTypeSubject.accept(true);
    }

    @Override
    public void onHorizontalHomeTypeClicked() {
        homeTypeSubject.accept(false);
    }
}
