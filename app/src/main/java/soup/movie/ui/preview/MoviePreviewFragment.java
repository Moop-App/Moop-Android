package soup.movie.ui.preview;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import soup.movie.R;
import soup.movie.data.Movie;

public class MoviePreviewFragment extends Fragment implements MoviePreviewContract.View {

    private MoviePreviewContract.Presenter mPresenter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MoviePreviewContract.AdapterView mAdapterView;

    public MoviePreviewFragment() {
        mPresenter = new MoviePreviewPresenter(this);
    }

    public static MoviePreviewFragment newInstance() {
        return new MoviePreviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_preview_list, container, false);

        Context context = view.getContext();

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.n_blue);
        swipeRefreshLayout.setOnRefreshListener(() -> mPresenter.refresh());
        mSwipeRefreshLayout = swipeRefreshLayout;

        MoviePreviewListAdapter adapterView = new MoviePreviewListAdapter();
        RecyclerView recyclerView = view.findViewById(R.id.preview_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapterView);
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        //TODO: insert equal spacing
        // refer to https://gist.github.com/alexfu/f7b8278009f3119f523a
        //recyclerView.addItemDecoration();
        mAdapterView = adapterView;

        mPresenter.bind();
        mPresenter.loadItems();

        return view;
    }

    @Override
    public void onDestroyView() {
        mPresenter.unbind();
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
    public void onClearList() {
        MoviePreviewContract.AdapterView adapterView = mAdapterView;
        if (adapterView != null) {
            adapterView.updateList(null);
        }
    }

    @Override
    public void onListUpdated(List<Movie> items) {
        MoviePreviewContract.AdapterView adapterView = mAdapterView;
        if (adapterView != null) {
            adapterView.updateList(items);
        }
    }

    @Override
    public void onRefreshDone() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
