package soup.movie.ui.detail;

import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import soup.movie.Injection;
import soup.movie.data.soup.model.TheaterCode;
import soup.movie.data.soup.model.TimeTable;
import soup.movie.data.soup.model.TimeTableRequest;
import soup.movie.data.soup.model.TimeTableResponse;
import soup.movie.data.soup.model.Trailer;
import soup.movie.data.soup.model.TrailerRequest;
import soup.movie.data.soup.model.TrailerResponse;
import soup.movie.data.utils.TheaterUtil;
import timber.log.Timber;

public class DetailPresenter implements DetailContract.Presenter {

    private DetailContract.View mView;
    private Disposable mDisposable;

    @Override
    public void attach(DetailContract.View view) {
        mView = view;
    }

    @Override
    public void detach() {
        Disposable disposable = mDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            mDisposable = null;
        }
        mView = null;
    }

    @Override
    public void requestData(@NonNull String movieId) {
        List<TheaterCode> theaters = TheaterUtil.getMyTheaterList();
        //mView.render(new DetailUiModel.Loading(!theaters.isEmpty()));
        if (theaters.isEmpty()) {
            mDisposable = getTrailerListObservable(movieId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            trailers -> mView.render(new DetailUiModel.Data(new TimeTable(null), trailers)),
                            Timber::e);
        } else {
            mDisposable = Single.zip(
                    getTimeTableObservable(theaters.get(0).getCode(), movieId),
                    getTrailerListObservable(movieId),
                    Pair::create)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            pair -> mView.render(new DetailUiModel.Data(pair.first, pair.second)),
                            Timber::e);
        }
    }

    @Override
    public void requestData(@NonNull String code, @NonNull String movieId) {
        mDisposable = Single.zip(
                getTimeTableObservable(code, movieId),
                getTrailerListObservable(movieId),
                Pair::create)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        pair -> mView.render(new DetailUiModel.Data(pair.first, pair.second)),
                        Timber::e);
    }

    private Single<TimeTable> getTimeTableObservable(@NonNull String theaterId, @NonNull String movieId) {
        return Injection.get()
                .getMovieRepository()
                .getTimeTableList(new TimeTableRequest(theaterId, movieId))
                .onErrorReturn(throwable -> {
                    TimeTableResponse response = new TimeTableResponse();
                    response.setTimeTable(new TimeTable(Collections.emptyList()));
                    return response;
                })
                .map(TimeTableResponse::getTimeTable);
    }

    private Single<List<Trailer>> getTrailerListObservable(@NonNull String movieId) {
        return Injection.get()
                .getMovieRepository()
                .getTrailerList(new TrailerRequest(movieId))
                .onErrorReturn(throwable -> {
                    TrailerResponse response = new TrailerResponse();
                    response.setTrailerList(Collections.emptyList());
                    return response;
                })
                .map(TrailerResponse::getTrailerList);
    }
}
