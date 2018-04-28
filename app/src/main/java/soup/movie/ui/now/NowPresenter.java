package soup.movie.ui.now;

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

public class NowPresenter implements NowContract.Presenter {

    private NowContract.View mView;

    private Disposable mDisposable;

    NowPresenter() {
    }

    @Override
    public void attach(NowContract.View view) {
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
                .subscribe(list -> mView.render(new NowViewState.Data(title, list)));
    }

    private Single<List<Movie>> getNowObservable() {
        return Injection.get().getMovieRepository()
                .getNowList(new NowMovieRequest())
                .map(NowMovieResponse::getList);
    }

    private Single<List<Movie>> getArtObservable() {
        return Injection.get().getMovieRepository()
                .getArtList(new ArtMovieRequest())
                .map(ArtMovieResponse::getList);

    }

    private Single<List<Movie>> getPlanObservable() {
        return Injection.get().getMovieRepository()
                .getPlanList(new PlanMovieRequest())
                .map(PlanMovieResponse::getList);

    }
}
