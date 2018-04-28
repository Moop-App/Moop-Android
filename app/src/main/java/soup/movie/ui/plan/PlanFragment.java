package soup.movie.ui.plan;

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
import soup.movie.data.soup.model.Movie;
import soup.movie.ui.main.MainTabFragment;
import soup.movie.ui.plan.PlanViewState.DoneState;
import soup.movie.ui.plan.PlanViewState.LoadingState;
import soup.widget.snappy.SnappyLinearLayoutManager;
import timber.log.Timber;

public class PlanFragment extends MainTabFragment implements PlanContract.View {

    private PlanContract.Presenter presenter;

    private PlanListAdapter adapterView;

    @BindView(R.id.list)
    RecyclerView listView;

    public PlanFragment() {
    }

    public static PlanFragment newInstance() {
        return new PlanFragment();
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

        PlanListAdapter adapterView = new PlanListAdapter(getActivity());
        RecyclerView recyclerView = listView;
        recyclerView.setLayoutManager(new SnappyLinearLayoutManager(context).horizontally());
        recyclerView.setAdapter(adapterView);
        recyclerView.setItemAnimator(new SlideInRightAnimator());
        recyclerView.getItemAnimator().setAddDuration(200);
        recyclerView.getItemAnimator().setRemoveDuration(200);
        this.adapterView = adapterView;

        presenter = new PlanPresenter();
        presenter.attach(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        presenter.detach();
        super.onDestroyView();
    }

    @Override
    public void render(@NonNull PlanViewState viewState) {
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
        PlanListAdapter adapterView = this.adapterView;
        if (adapterView != null) {
            adapterView.updateList(movieList);
        }
    }
}
