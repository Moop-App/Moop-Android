package soup.movie.ui;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<View extends BaseContract.View>
        implements BaseContract.Presenter<View> {

    protected View view;

    private CompositeDisposable subscriptions;

    @Override
    public void attach(View view) {
        this.view = view;
        subscriptions = new CompositeDisposable();
        initObservable(subscriptions);
    }

    @Override
    public void detach() {
        subscriptions.dispose();
        subscriptions = null;
        this.view = null;
    }

    protected void initObservable(CompositeDisposable subscriptions) {
    }

    protected final void register(Disposable disposable) {
        subscriptions.add(disposable);
    }
}
