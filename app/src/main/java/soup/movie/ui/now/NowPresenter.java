package soup.movie.ui.now;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import soup.movie.Injection;
import soup.movie.data.soup.model.Movie;
import soup.movie.data.soup.model.NowMovieRequest;
import soup.movie.data.soup.model.NowMovieResponse;

public class NowPresenter implements NowContract.Presenter {

    private NowContract.View view;

    private Disposable disposable;

    NowPresenter() {
    }

    @Override
    public void attach(NowContract.View view) {
        this.view = view;
        loadMovieList(getNowObservable());
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
        disposable = movieObservable
                .observeOn(AndroidSchedulers.mainThread())
                .map(NowViewState.DoneState::new)
                .subscribe(view::render);
    }

    private Single<List<Movie>> getNowObservable() {
        return Injection.get().getMovieRepository()
                .getNowList(new NowMovieRequest())
                .map(NowMovieResponse::getList);
    }
}
