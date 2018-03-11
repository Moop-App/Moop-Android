package soup.movie.ui.boxoffice;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import soup.movie.Injection;
import soup.movie.data.DailyBoxOfficeRequest;

public class BoxOfficePresenter implements BoxOfficeContract.Presenter {

    private Injection mInjection;
    private BoxOfficeContract.View mView;

    private Disposable mDisposable;

    BoxOfficePresenter() {
        mInjection = new Injection();
    }

    @Override
    public void attach(BoxOfficeContract.View view) {
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
                .getDailyBoxOfficeList(new DailyBoxOfficeRequest())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(boxOfficeList -> mView.render(new BoxOfficeUiModel.Data(boxOfficeList)));
    }
}
