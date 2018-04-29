package soup.movie.ui.main.plan;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import soup.movie.Injection;
import soup.movie.data.model.Movie;
import soup.movie.data.model.PlanMovieRequest;
import soup.movie.data.model.PlanMovieResponse;

public class PlanPresenter implements PlanContract.Presenter {

    private PlanContract.View view;

    private Disposable disposable;

    PlanPresenter() {
    }

    @Override
    public void attach(PlanContract.View view) {
        this.view = view;
        loadMovieList(getPlanObservable());
    }

    @Override
    public void detach() {
        view = null;
        Disposable disposable = this.disposable;
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private void loadMovieList(Single<List<Movie>> movieObservable) {
        disposable = movieObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> view.render(new PlanViewState.DoneState(list)));
    }

    private Single<List<Movie>> getPlanObservable() {
        return Injection.get().getMovieRepository()
                .getPlanList(new PlanMovieRequest())
                .map(PlanMovieResponse::getList);

    }
}
