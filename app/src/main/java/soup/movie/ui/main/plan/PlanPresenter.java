package soup.movie.ui.main.plan;

import com.jakewharton.rxrelay2.BehaviorRelay;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import soup.movie.data.MovieRepository;
import soup.movie.data.model.PlanMovieRequest;
import soup.movie.data.model.PlanMovieResponse;
import soup.movie.di.FragmentScoped;
import soup.movie.ui.BasePresenter;

@FragmentScoped
public class PlanPresenter extends BasePresenter<PlanContract.View> implements PlanContract.Presenter {

    private final MovieRepository movieRepository;

    private BehaviorRelay<Boolean> refreshRelay = BehaviorRelay.createDefault(true);

    @Inject
    PlanPresenter(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    protected void initObservable(CompositeDisposable subscriptions) {
        subscriptions.add(getViewStateObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::render));
    }

    private Flowable<PlanViewState> getViewStateObservable() {
        return refreshRelay.toFlowable(BackpressureStrategy.LATEST)
                .flatMap(ignore -> movieRepository.getPlanList(new PlanMovieRequest())
                        .map(PlanMovieResponse::getList)
                        .map(PlanViewState.DoneState::new)
                        .toFlowable());
    }

    @Override
    public void refresh() {
        refreshRelay.accept(true);
    }
}
