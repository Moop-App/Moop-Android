package soup.movie.ui.main.now;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import soup.movie.R;
import soup.movie.ui.detail.DetailActivity;
import soup.movie.ui.main.BaseTabFragment;
import soup.movie.ui.main.now.NowContract.Presenter;
import soup.movie.ui.main.now.NowContract.View;
import soup.movie.ui.main.now.NowViewState.DoneState;
import soup.movie.ui.main.now.NowViewState.LoadingState;
import soup.movie.util.MovieUtil;
import soup.movie.util.ViewUtil;
import timber.log.Timber;

import static soup.movie.util.RecyclerViewUtil.gridLayoutManager;

public class NowFragment extends BaseTabFragment<View, Presenter> implements View {

    public static NowFragment newInstance() {
        return new NowFragment();
    }

    @Inject
    NowContract.Presenter presenter;

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.list)
    RecyclerView listView;

    private NowListAdapter listAdapter;

    public NowFragment() {
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_vertical_list;
    }

    @NonNull
    @Override
    protected NowContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected void initViewState(@NonNull Context ctx) {
        super.initViewState(ctx);
        listAdapter = new NowListAdapter((movie, sharedElements) -> {
            Activity activity = getActivity();
            Intent intent = new Intent(activity, DetailActivity.class);
            MovieUtil.saveTo(intent, movie);
            activity.startActivity(intent, ActivityOptions
                    .makeSceneTransitionAnimation(activity, sharedElements)
                    .toBundle());
        });
        listView.setLayoutManager(gridLayoutManager(ctx, 3));
        listView.setAdapter(listAdapter);
        listView.setItemAnimator(new SlideInUpAnimator());
        listView.getItemAnimator().setAddDuration(200);
        listView.getItemAnimator().setRemoveDuration(200);
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.refresh());
    }

    @Override
    public void render(@NonNull NowViewState viewState) {
        Timber.i("render: %s", viewState);
        if (viewState instanceof LoadingState) {
            renderLoadingState();
        } else if (viewState instanceof DoneState) {
            renderDoneState((DoneState)viewState);
        } else {
            throw new IllegalStateException("Unknown view state(" + viewState + ")");
        }
    }

    private void renderLoadingState() {
        swipeRefreshLayout.setRefreshing(true);
        ViewUtil.hide(listView);
    }

    private void renderDoneState(@NonNull DoneState state) {
        swipeRefreshLayout.setRefreshing(false);
        listAdapter.submitList(state.getMovies());
        ViewUtil.show(listView);
    }
}
