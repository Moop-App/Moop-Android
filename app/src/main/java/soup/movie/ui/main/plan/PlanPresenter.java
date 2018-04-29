package soup.movie.ui.main.plan;

import javax.inject.Inject;

import io.reactivex.Single;
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

    private Single<PlanViewState> getViewStateObservable() {
        return movieRepository.getPlanList(new PlanMovieRequest())
                .map(PlanMovieResponse::getList)
                .map(PlanViewState.DoneState::new);
    }
}
