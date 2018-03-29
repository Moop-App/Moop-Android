package soup.movie.ui.archive;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import soup.movie.Injection;
import soup.movie.data.kobis.model.MovieListRequest;

public class ArchivePresenter implements ArchiveContract.Presenter {

    private ArchiveContract.View mView;

    private Disposable mDisposable;

    ArchivePresenter() {
    }

    @Override
    public void attach(ArchiveContract.View view) {
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

    private void loadMovieList() {
        mDisposable = Injection.get().getMovieRepository()
                .getMovieList(new MovieListRequest())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieList -> mView.render(new ArchiveUiModel.Data(movieList)));
    }
}
