package soup.movie.ui.boxoffice;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import soup.movie.Injection;
import soup.movie.data.DailyBoxOfficeRequest;
import soup.movie.data.DailyBoxOfficeResult;
import soup.movie.data.source.MovieRepository;

import static soup.movie.util.TimeUtil.today;
import static soup.movie.util.TimeUtil.yesterday;

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
        MovieRepository movieRepository = mInjection.getMovieRepository();
        Single<DailyBoxOfficeResult> todayBoxOffice = movieRepository.getDailyBoxOfficeList(
                new DailyBoxOfficeRequest().setTargetDt(today()));
        Single<DailyBoxOfficeResult> yesterdayBoxOffice = movieRepository.getDailyBoxOfficeList(
                new DailyBoxOfficeRequest().setTargetDt(yesterday()));
        mDisposable = Single.concat(todayBoxOffice, yesterdayBoxOffice)
                .observeOn(AndroidSchedulers.mainThread())
                .take(1)
                .doOnSubscribe(notUse -> mView.render(new BoxOfficeUiModel.InProgress()))
                .subscribe(result -> {
                    String title = result.getBoxOfficeType() + ": " + result.getShowRange().substring(0, 8);
                    mView.render(new BoxOfficeUiModel.Data(
                            title, result.getDailyBoxOfficeList()));
                }, throwable -> {
                });
    }
}
