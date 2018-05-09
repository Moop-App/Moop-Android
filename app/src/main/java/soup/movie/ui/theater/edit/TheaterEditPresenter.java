package soup.movie.ui.theater.edit;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import soup.movie.data.MovieRepository;
import soup.movie.data.model.Area;
import soup.movie.data.model.CodeRequest;
import soup.movie.data.model.TheaterCode;
import soup.movie.di.ActivityScoped;
import soup.movie.settings.TheaterSetting;
import soup.movie.ui.BasePresenter;

@ActivityScoped
public class TheaterEditPresenter extends BasePresenter<TheaterEditContract.View>
        implements TheaterEditContract.Presenter {

    private final MovieRepository movieRepository;
    private final TheaterSetting theaterSetting;

    @Inject
    TheaterEditPresenter(MovieRepository movieRepository, TheaterSetting theaterSetting) {
        this.movieRepository = movieRepository;
        this.theaterSetting = theaterSetting;
    }

    @Override
    protected void initObservable(CompositeDisposable subscriptions) {
        subscriptions.add(getViewStateObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::render));
    }

    private Single<TheaterEditViewState> getViewStateObservable() {
        return Single.zip(
                getAllTheatersObservable(),
                getSelectedTheatersObservable(),
                TheaterEditViewState::new);
    }

    private Single<List<TheaterCode>> getAllTheatersObservable() {
        return movieRepository.getCodeList(new CodeRequest())
                .toObservable()
                .flatMapIterable(response -> {
                    ArrayList<Area> areas = new ArrayList<>();
                    areas.addAll(response.getCgvGroup().getList());
                    areas.addAll(response.getLotteGroup().getList());
                    areas.addAll(response.getMegaboxGroup().getList());
                    return areas;
                })
                .flatMapIterable(Area::getTheaterList)
                .toList();
    }

    private Single<List<TheaterCode>> getSelectedTheatersObservable() {
        return Single.just(theaterSetting.getFavoriteTheaters());
    }

    @Override
    public void onConfirmClicked(List<TheaterCode> selectedTheaters) {
        theaterSetting.setFavoriteTheaters(selectedTheaters);
    }
}
