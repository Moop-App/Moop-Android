package soup.movie.ui.detail;

import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import soup.movie.data.MovieRepository;
import soup.movie.data.model.Movie;
import soup.movie.data.model.TheaterCode;
import soup.movie.data.model.TimeTable;
import soup.movie.data.model.TimeTableRequest;
import soup.movie.data.model.TimeTableResponse;
import soup.movie.data.model.Trailer;
import soup.movie.di.ActivityScoped;
import soup.movie.util.TheaterUtil;
import timber.log.Timber;

@ActivityScoped
public class DetailPresenter implements DetailContract.Presenter {

    private final MovieRepository movieRepository;

    private DetailContract.View view;
    private Disposable disposable;

    @Inject
    DetailPresenter(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void attach(DetailContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        Disposable disposable = this.disposable;
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            this.disposable = null;
        }
        view = null;
    }

    @Override
    public void requestData(@NonNull Movie movie) {
        List<TheaterCode> theaters = TheaterUtil.getMyTheaterList();
        //view.render(new DetailViewState.LoadingState(!theaters.isEmpty()));
        if (theaters.isEmpty()) {
            disposable = getTrailerListObservable(movie)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            trailers -> view.render(new DetailViewState.DoneState(new TimeTable(null), trailers)),
                            Timber::e);
        } else {
            disposable = Single.zip(
                    getTimeTableObservable(theaters.get(0).getCode(), movie.getId()),
                    getTrailerListObservable(movie),
                    Pair::create)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            pair -> view.render(new DetailViewState.DoneState(pair.first, pair.second)),
                            Timber::e);
        }
    }

    @Override
    public void requestData(@NonNull String code, @NonNull Movie movie) {
        disposable = Single.zip(
                getTimeTableObservable(code, movie.getId()),
                getTrailerListObservable(movie),
                Pair::create)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        pair -> view.render(new DetailViewState.DoneState(pair.first, pair.second)),
                        Timber::e);
    }

    private Single<TimeTable> getTimeTableObservable(@NonNull String theaterId, @NonNull String movieId) {
        return movieRepository
                .getTimeTableList(new TimeTableRequest(theaterId, movieId))
                .onErrorReturn(throwable -> {
                    TimeTableResponse response = new TimeTableResponse();
                    response.setTimeTable(new TimeTable(Collections.emptyList()));
                    return response;
                })
                .map(TimeTableResponse::getTimeTable);
    }

    private Single<List<Trailer>> getTrailerListObservable(@NonNull Movie movie) {
        return Single.just(movie.getTrailers())
                .onErrorReturn(throwable -> Collections.emptyList());
    }
}
