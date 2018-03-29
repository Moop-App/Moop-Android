package soup.movie.ui.detail;

import android.support.annotation.NonNull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import soup.movie.Injection;
import soup.movie.data.soup.model.TrailerRequest;
import soup.movie.data.soup.model.TrailerResponse;
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
        mView.render(new DetailUiModel.Loading());
        mDisposable = Injection.get()
                .getMovieRepository()
                .getTrailerList(new TrailerRequest(movieId))
                .observeOn(AndroidSchedulers.mainThread())
                .map(TrailerResponse::getTrailerList)
                .subscribe(
                        trailers -> mView.render(new DetailUiModel.Done(trailers)),
                        Timber::e);
    }
}
