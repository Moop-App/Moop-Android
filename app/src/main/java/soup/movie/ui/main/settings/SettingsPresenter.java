package soup.movie.ui.main.settings;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import soup.movie.data.MovieRepository;
import soup.movie.data.model.Area;
import soup.movie.data.model.CodeRequest;
import soup.movie.data.model.CodeResponse;
import soup.movie.data.model.TheaterCode;
import soup.movie.ui.BasePresenter;
import soup.movie.ui.main.settings.SettingsViewState.DoneState;
import timber.log.Timber;

public class SettingsPresenter extends BasePresenter<SettingsContract.View>
        implements SettingsContract.Presenter {

    private final MovieRepository movieRepository;
    private final SharedPreferences preferences;

    @Inject
    SettingsPresenter(MovieRepository movieRepository,
                      SharedPreferences preferences) {
        this.movieRepository = movieRepository;
        this.preferences = preferences;
    }

    @Override
    protected void initObservable(CompositeDisposable subscriptions) {
        subscriptions.add(getViewStateObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::render, Timber::e));
    }

    private Single<SettingsViewState> getViewStateObservable() {
        return movieRepository.getCodeList(new CodeRequest())
                .map(CodeResponse::getList)
                .map(areas -> {
                    List<TheaterCode> codes = new ArrayList<>();
                    for (Area area : areas) {
                        codes.addAll(area.getTheaterList());
                    }
                    return codes;
                })
                .map(DoneState::new);
    }
}
