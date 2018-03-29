package soup.movie.ui.home;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import soup.movie.Injection;
import soup.movie.data.soup.model.ArtMovieRequest;
import soup.movie.data.soup.model.ArtMovieResponse;
import soup.movie.data.soup.model.Movie;
import soup.movie.data.soup.model.NowMovieRequest;
import soup.movie.data.soup.model.NowMovieResponse;
import soup.movie.data.soup.model.PlanMovieRequest;
import soup.movie.data.soup.model.PlanMovieResponse;

public class HomePresenter implements HomeContract.Presenter {

    private Injection mInjection;
    private HomeContract.View mView;

    private Disposable mDisposable;

    HomePresenter() {
        mInjection = new Injection();
    }

    @Override
    public void attach(HomeContract.View view) {
        mView = view;
    }

    @Override
    public void detach() {
        mView = null;
        Disposable disposable = mDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void requestMovieList(Type type) {
        final String title = type.value();
        switch (type) {
            case NOW:
                loadMovieList(title, getNowObservable());
                break;
            case ART:
                loadMovieList(title, getArtObservable());
                break;
            case PLAN:
                loadMovieList(title, getPlanObservable());
                break;
        }
    }

    private void loadMovieList(final String title, Single<List<Movie>> movieObservable) {
        mDisposable = movieObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> mView.render(new HomeUiModel.Data(title, list)));
    }

    private Single<List<Movie>> getNowObservable() {
        return mInjection.getMovieRepository()
                .getNowList(new NowMovieRequest())
                .map(NowMovieResponse::getList);
    }

    private Single<List<Movie>> getArtObservable() {
        return mInjection.getMovieRepository()
                .getArtList(new ArtMovieRequest())
                .map(ArtMovieResponse::getList);

    }

    private Single<List<Movie>> getPlanObservable() {
        return mInjection.getMovieRepository()
                .getPlanList(new PlanMovieRequest())
                .map(PlanMovieResponse::getList);

    }
}
