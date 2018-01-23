package soup.movie.ui.preview;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import soup.movie.DummyContents;

public class MoviePreviewPresenter implements MoviePreviewContract.Presenter {

    private MoviePreviewContract.View mView;

    MoviePreviewPresenter(MoviePreviewContract.View view) {
        mView = view;
    }

    @Override
    public void bind() {
    }

    @Override
    public void unbind() {
    }

    @Override
    public void refresh() {
        mView.onClearList();
        Observable.timer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ignore -> {
                    mView.onRefreshDone();
                    mView.onListUpdated(Arrays.asList(DummyContents.ITEMS));
                });
    }

    @Override
    public void loadItems() {
        mView.onListUpdated(Arrays.asList(DummyContents.ITEMS));
    }
}
