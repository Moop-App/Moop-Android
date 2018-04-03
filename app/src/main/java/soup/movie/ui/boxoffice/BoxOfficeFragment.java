package soup.movie.ui.boxoffice;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import soup.movie.R;
import soup.movie.data.kobis.model.BoxOfficeMovie;
import soup.movie.ui.main.MainTabFragment;
import timber.log.Timber;

public class BoxOfficeFragment extends MainTabFragment implements BoxOfficeContract.View {

    private BoxOfficeContract.Presenter mPresenter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private BoxOfficeListAdapter mAdapterView;

    public BoxOfficeFragment() {
    }

    public static BoxOfficeFragment newInstance() {
        return new BoxOfficeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_with_pull_to_request, container, false);

        Context context = view.getContext();

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.n_blue);
        swipeRefreshLayout.setOnRefreshListener(() -> mPresenter.refresh());
        mSwipeRefreshLayout = swipeRefreshLayout;

        BoxOfficeListAdapter adapterView = new BoxOfficeListAdapter();
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapterView);
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        mAdapterView = adapterView;

        mPresenter = new BoxOfficePresenter();
        mPresenter.attach(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        mPresenter.detach();
        super.onDestroyView();
    }

    @Override
    protected int getMenuResource() {
        return R.menu.menu_boxoffice;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                showSubPanel(getString(R.string.action_filter));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void render(BoxOfficeUiModel uiModel) {
        Timber.i("render: %s", uiModel);
        if (uiModel instanceof BoxOfficeUiModel.InProgress) {
            mSwipeRefreshLayout.setRefreshing(true);
            updateMovieList(null);
        } else if (uiModel instanceof BoxOfficeUiModel.Data) {
            mSwipeRefreshLayout.setRefreshing(false);
            BoxOfficeUiModel.Data data = (BoxOfficeUiModel.Data) uiModel;
            setTitle(data.getTitle());
            updateMovieList(data.getMovies());
        } else if (uiModel instanceof BoxOfficeUiModel.Empty) {
            mSwipeRefreshLayout.setRefreshing(false);
            updateMovieList(null);
        } else {
            throw new IllegalStateException("Unknown UI Model");
        }
    }

    private void updateMovieList(@Nullable List<BoxOfficeMovie> movieList) {
        BoxOfficeListAdapter adapterView = mAdapterView;
        if (adapterView != null) {
            adapterView.updateList(movieList);
        }
    }
}
