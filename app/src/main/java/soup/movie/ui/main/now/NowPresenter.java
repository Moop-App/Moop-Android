package soup.movie.ui.main.now;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import soup.movie.data.MovieRepository;
import soup.movie.data.model.Movie;
import soup.movie.data.model.NowMovieRequest;
import soup.movie.data.model.NowMovieResponse;
import soup.movie.di.FragmentScoped;

@FragmentScoped
public class NowPresenter implements NowContract.Presenter {

    private final MovieRepository movieRepository;

    private NowContract.View view;

    private Disposable disposable;

    @Inject
    NowPresenter(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void attach(NowContract.View view) {
        this.view = view;
        loadMovieList(getNowObservable());
    }

    @Override
    public void detach() {
        this.view = null;
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
        return movieRepository
                .getNowList(new NowMovieRequest())
                .map(NowMovieResponse::getList);
    }
}
