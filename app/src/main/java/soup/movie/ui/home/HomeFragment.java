package soup.movie.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import soup.movie.R;
import timber.log.Timber;

public class HomeFragment extends Fragment implements HomeContract.View {

    private HomeContract.Presenter mPresenter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private HomeListAdapter mAdapterView;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
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

        HomeListAdapter adapterView = new HomeListAdapter();
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapterView);
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        mAdapterView = adapterView;

        mPresenter = new HomePresenter();
        mPresenter.attach(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        mPresenter.detach();
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void render(HomeUiModel uiModel) {
        Timber.i("render: %s", uiModel);
        if (uiModel instanceof HomeUiModel.InProgress) {
            HomeListAdapter adapterView = mAdapterView;
            if (adapterView != null) {
                adapterView.updateList(null);
            }
        } else if (uiModel instanceof HomeUiModel.Data) {
            HomeUiModel.Data data = (HomeUiModel.Data)uiModel;
            mSwipeRefreshLayout.setRefreshing(false);
            HomeListAdapter adapterView = mAdapterView;
            if (adapterView != null) {
                adapterView.updateList(data.getMovies());
            }
        } else {
            throw new IllegalStateException("Unknown UI Model");
        }
    }
}
