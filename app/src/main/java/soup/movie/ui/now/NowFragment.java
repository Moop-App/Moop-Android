package soup.movie.ui.now;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;
import soup.movie.R;
import soup.widget.snappy.SnappyLinearLayoutManager;
import soup.movie.data.soup.model.Movie;
import soup.movie.ui.main.MainTabFragment;
import soup.movie.ui.now.NowViewState.DoneState;
import soup.movie.ui.now.NowViewState.LoadingState;
import timber.log.Timber;

public class NowFragment extends MainTabFragment implements NowContract.View {

    private NowContract.Presenter mPresenter;

    private NowListAdapter mAdapterView;

    @BindView(R.id.list)
    RecyclerView mListView;

    public NowFragment() {
    }

    public static NowFragment newInstance() {
        return new NowFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        Context context = view.getContext();

        NowListAdapter adapterView = new NowListAdapter(getActivity());
        RecyclerView recyclerView = mListView;
        recyclerView.setLayoutManager(new SnappyLinearLayoutManager(context).horizontally());
        recyclerView.setAdapter(adapterView);
        recyclerView.setItemAnimator(new SlideInRightAnimator());
        recyclerView.getItemAnimator().setAddDuration(200);
        recyclerView.getItemAnimator().setRemoveDuration(200);
        mAdapterView = adapterView;

        mPresenter = new NowPresenter();
        mPresenter.attach(this);
        mPresenter.requestMovieList(NowContract.Presenter.Type.NOW);

        return view;
    }

    @Override
    public void onDestroyView() {
        mPresenter.detach();
        super.onDestroyView();
    }

    @Override
    public void render(@NonNull NowViewState viewState) {
        Timber.i("render: %s", viewState);
        if (viewState instanceof LoadingState) {
            renderInternal((LoadingState) viewState);
        } else if (viewState instanceof DoneState) {
            renderInternal((DoneState)viewState);
        } else {
            throw new IllegalStateException("Unknown UI Model");
        }
    }

    private void renderInternal(@NonNull LoadingState viewState) {
        updateMovieList(null);
    }

    private void renderInternal(@NonNull DoneState viewState) {
        updateMovieList(viewState.getMovies());
    }

    private void updateMovieList(@Nullable List<Movie> movieList) {
        NowListAdapter adapterView = mAdapterView;
        if (adapterView != null) {
            adapterView.updateList(movieList);
        }
    }
}
