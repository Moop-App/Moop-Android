package soup.movie.ui.theater.sort;

import java.util.List;

import javax.inject.Inject;

import soup.movie.data.model.TheaterCode;
import soup.movie.di.ActivityScoped;
import soup.movie.settings.TheaterSetting;
import soup.movie.ui.BasePresenter;

@ActivityScoped
public class TheaterSortPresenter extends BasePresenter<TheaterSortContract.View>
        implements TheaterSortContract.Presenter {

    private final TheaterSetting theaterSetting;

    @Inject
    TheaterSortPresenter(TheaterSetting theaterSetting) {
        this.theaterSetting = theaterSetting;
    }

    @Override
    public void attach(TheaterSortContract.View view) {
        super.attach(view);
        view.render(new TheaterSortViewState(theaterSetting.getFavoriteTheaters()));
    }

    @Override
    public void onConfirmClicked(List<TheaterCode> selectedTheaters) {
        theaterSetting.setFavoriteTheaters(selectedTheaters);
    }
}
