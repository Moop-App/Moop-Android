package soup.movie.ui.main.now;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import soup.movie.data.MovieRepository;
import soup.movie.data.model.NowMovieRequest;
import soup.movie.data.model.NowMovieResponse;
import soup.movie.di.FragmentScoped;
import soup.movie.ui.BasePresenter;

@FragmentScoped
public class NowPresenter extends BasePresenter<NowContract.View>
        implements NowContract.Presenter {

    private final MovieRepository movieRepository;

    @Inject
    NowPresenter(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    protected void initObservable(CompositeDisposable subscriptions) {
        subscriptions.add(getViewStateObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::render));
    }

    private Single<NowViewState> getViewStateObservable() {
        return movieRepository.getNowList(new NowMovieRequest())
                .map(NowMovieResponse::getList)
                .map(NowViewState.DoneState::new);
    }
}
