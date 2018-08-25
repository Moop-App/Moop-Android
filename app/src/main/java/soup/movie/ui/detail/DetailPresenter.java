package soup.movie.ui.detail;

import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import soup.movie.data.MoobRepository;
import soup.movie.data.model.Movie;
import soup.movie.data.model.Theater;
import soup.movie.data.model.TimeTable;
import soup.movie.data.request.TimeTableRequest;
import soup.movie.data.response.TimeTableResponse;
import soup.movie.data.model.Trailer;
import soup.movie.di.scope.ActivityScoped;
import soup.movie.settings.TheaterSetting;
import soup.movie.ui.BasePresenter;
import timber.log.Timber;

@ActivityScoped
public class DetailPresenter extends BasePresenter<DetailContract.View>
        implements DetailContract.Presenter {

    private final MoobRepository moobRepository;
    private final TheaterSetting theaterSetting;

    @Inject
    DetailPresenter(MoobRepository moobRepository,
                    TheaterSetting theaterSetting) {
        this.moobRepository = moobRepository;
        this.theaterSetting = theaterSetting;
    }

    @Override
    public void requestData(@NonNull Movie movie) {
        List<Theater> theaters = theaterSetting.getFavoriteTheaters();
        //view.render(new DetailViewState.LoadingState(!theaters.isEmpty()));
        if (theaters.isEmpty()) {
            register(getTrailerListObservable(movie)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            trailers -> view.render(new DetailViewState.DoneState(
                                    new TimeTable(Collections.emptyList()), trailers)),
                            Timber::e));
        } else {
            register(Single.zip(
                    getTimeTableObservable(theaters.get(0).getCode(), movie.getId()),
                    getTrailerListObservable(movie),
                    Pair::create)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            pair -> view.render(new DetailViewState.DoneState(pair.first, pair.second)),
                            Timber::e));
        }
    }

    @Override
    public void requestData(@NonNull String code, @NonNull Movie movie) {
        register(Single.zip(
                getTimeTableObservable(code, movie.getId()),
                getTrailerListObservable(movie),
                Pair::create)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        pair -> view.render(new DetailViewState.DoneState(pair.first, pair.second)),
                        Timber::e));
    }

    private Single<TimeTable> getTimeTableObservable(@NonNull String theaterId, @NonNull String movieId) {
        return moobRepository
                .getTimeTableList(new TimeTableRequest(theaterId, movieId))
                .onErrorReturn(throwable -> new TimeTableResponse(new TimeTable(Collections.emptyList())))
                .map(TimeTableResponse::getTimeTable);
    }

    private Single<List<Trailer>> getTrailerListObservable(@NonNull Movie movie) {
        return Single.just(movie.getTrailers())
                .onErrorReturn(throwable -> Collections.emptyList());
    }
}
