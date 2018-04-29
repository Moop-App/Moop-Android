package soup.movie.ui.main.now;

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
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;
import soup.movie.R;
import soup.widget.snappy.SnappyLinearLayoutManager;
import soup.movie.data.model.Movie;
import soup.movie.ui.main.MainTabFragment;
import soup.movie.ui.main.now.NowViewState.DoneState;
import soup.movie.ui.main.now.NowViewState.LoadingState;
import timber.log.Timber;

public class NowFragment extends MainTabFragment implements NowContract.View {

    private NowContract.Presenter presenter;

    private NowListAdapter adapterView;

    @BindView(R.id.list)
    RecyclerView listView;

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
        return inflater.inflate(R.layout.fragment_horizontal_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = view.getContext();

        adapterView = new NowListAdapter(getActivity());
        RecyclerView recyclerView = listView;
        recyclerView.setLayoutManager(new SnappyLinearLayoutManager(context).horizontally());
        recyclerView.setAdapter(adapterView);
        recyclerView.setItemAnimator(new SlideInRightAnimator());
        recyclerView.getItemAnimator().setAddDuration(200);
        recyclerView.getItemAnimator().setRemoveDuration(200);

        presenter = new NowPresenter();
        presenter.attach(this);
    }

    @Override
    public void onDestroyView() {
        presenter.detach();
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
        NowListAdapter adapterView = this.adapterView;
        if (adapterView != null) {
            adapterView.updateList(movieList);
        }
    }
}
