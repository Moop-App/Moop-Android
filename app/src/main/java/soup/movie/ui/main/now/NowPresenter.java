package soup.movie.ui.main.now;

import com.jakewharton.rxrelay2.BehaviorRelay;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
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

    private BehaviorRelay<Boolean> refreshRelay = BehaviorRelay.createDefault(true);

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

    private Flowable<NowViewState> getViewStateObservable() {
        return refreshRelay.toFlowable(BackpressureStrategy.LATEST)
                .flatMap(ignore -> movieRepository.getNowList(new NowMovieRequest())
                        .map(NowMovieResponse::getList)
                        .map(NowViewState.DoneState::new)
                        .toFlowable());
    }

    @Override
    public void refresh() {
        refreshRelay.accept(true);
    }
}
