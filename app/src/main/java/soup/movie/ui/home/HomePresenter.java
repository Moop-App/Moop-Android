package soup.movie.ui.home;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import soup.movie.Injection;
import soup.movie.data.MovieListRequest;

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
        loadMovieList();
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
    public void refresh() {
        loadMovieList();
    }

    private void loadMovieList() {
        mDisposable = mInjection.getMovieRepository()
                .getMovieList(new MovieListRequest())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieList -> mView.render(new HomeUiModel.Data(movieList)));
    }
}
