package soup.movie.ui.theater.edit;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import soup.movie.data.MoobRepository;
import soup.movie.data.model.AreaGroup;
import soup.movie.data.model.Theater;
import soup.movie.data.request.CodeRequest;
import soup.movie.di.scope.ActivityScoped;
import soup.movie.settings.TheaterSetting;
import soup.movie.ui.BasePresenter;

@ActivityScoped
public class TheaterEditPresenter extends BasePresenter<TheaterEditContract.View>
        implements TheaterEditContract.Presenter {

    private final MoobRepository moobRepository;
    private final TheaterSetting theaterSetting;

    @Inject
    TheaterEditPresenter(MoobRepository moobRepository, TheaterSetting theaterSetting) {
        this.moobRepository = moobRepository;
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

    private Single<List<Theater>> getAllTheatersObservable() {
        return moobRepository.getCodeList(CodeRequest.INSTANCE)
                .toObservable()
                .flatMapIterable(response -> {
                    ArrayList<AreaGroup> areas = new ArrayList<>();
                    areas.addAll(response.getCgv().getList());
                    areas.addAll(response.getLotte().getList());
                    areas.addAll(response.getMegabox().getList());
                    return areas;
                })
                .flatMapIterable(AreaGroup::getTheaterList)
                .toList();
    }

    private Single<List<Theater>> getSelectedTheatersObservable() {
        return Single.just(theaterSetting.getFavoriteTheaters());
    }

    @Override
    public void onConfirmClicked(List<Theater> selectedTheaters) {
        theaterSetting.setFavoriteTheaters(selectedTheaters);
    }
}
