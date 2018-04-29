package soup.movie.ui.main.settings;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import soup.movie.data.MovieRepository;
import soup.movie.data.model.Area;
import soup.movie.data.model.CodeRequest;
import soup.movie.data.model.CodeResponse;
import soup.movie.data.model.TheaterCode;
import soup.movie.ui.main.settings.SettingsViewState.DoneState;
import timber.log.Timber;

public class SettingsPresenter implements SettingsContract.Presenter {

    private final MovieRepository movieRepository;

    private SettingsContract.View view;
    private Disposable disposable;

    @Inject
    SettingsPresenter(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void attach(SettingsContract.View view) {
        this.view = view;
        disposable = movieRepository
                .getCodeList(new CodeRequest())
                .map(CodeResponse::getList)
                .map(areas -> {
                    List<TheaterCode> codes = new ArrayList<>();
                    for (Area area : areas) {
                        codes.addAll(area.getTheaterList());
                    }
                    Timber.d("loadAsync: complete, data=%s", codes);
                    return codes;
                })
                .map(DoneState::new)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::render, Timber::e);
    }

    @Override
    public void detach() {
        Disposable disposable = this.disposable;
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            this.disposable = null;
        }
        view = null;
    }
}
