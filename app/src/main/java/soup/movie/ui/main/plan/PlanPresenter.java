package soup.movie.ui.main.plan;

import com.jakewharton.rxrelay2.BehaviorRelay;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import soup.movie.data.MoobRepository;
import soup.movie.data.request.PlanMovieRequest;
import soup.movie.data.response.PlanMovieResponse;
import soup.movie.di.scope.FragmentScoped;
import soup.movie.ui.BasePresenter;

@FragmentScoped
public class PlanPresenter extends BasePresenter<PlanContract.View> implements PlanContract.Presenter {

    private final MoobRepository moobRepository;

    private BehaviorRelay<Boolean> refreshRelay = BehaviorRelay.createDefault(true);

    @Inject
    PlanPresenter(MoobRepository moobRepository) {
        this.moobRepository = moobRepository;
    }

    @Override
    protected void initObservable(CompositeDisposable subscriptions) {
        subscriptions.add(getViewStateObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::render));
    }

    private Flowable<PlanViewState> getViewStateObservable() {
        return refreshRelay.toFlowable(BackpressureStrategy.LATEST)
                .flatMap(ignore -> moobRepository.getPlanList(PlanMovieRequest.INSTANCE)
                        .map(PlanMovieResponse::getList)
                        .map(PlanViewState.DoneState::new)
                        .toFlowable());
    }

    @Override
    public void refresh() {
        refreshRelay.accept(true);
    }
}
