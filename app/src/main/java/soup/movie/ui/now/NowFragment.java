package soup.movie.ui.now;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;
import soup.movie.R;
import soup.movie.common.widget.snappy.SnappyLinearLayoutManager;
import soup.movie.data.soup.model.Movie;
import soup.movie.ui.main.MainTabFragment;
import timber.log.Timber;

public class NowFragment extends MainTabFragment implements NowContract.View {

    private NowContract.Presenter mPresenter;

    private NowListAdapter mAdapterView;

    @BindView(R.id.title)
    TextView mTitleView;

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
        View view = inflater.inflate(R.layout.list_with_title, container, false);
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
    protected int getMenuResource() {
        return R.menu.menu_now;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                showSubPanel(getString(R.string.action_page_settings));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void render(NowViewState uiModel) {
        Timber.i("render: %s", uiModel);
        if (uiModel instanceof NowViewState.InProgress) {
            updateMovieList(null);
        } else if (uiModel instanceof NowViewState.Data) {
            NowViewState.Data data = (NowViewState.Data)uiModel;
            mTitleView.setText(data.getTitle());
            updateMovieList(data.getMovies());
        } else {
            throw new IllegalStateException("Unknown UI Model");
        }
    }

    private void updateMovieList(@Nullable List<Movie> movieList) {
        NowListAdapter adapterView = mAdapterView;
        if (adapterView != null) {
            adapterView.updateList(movieList);
        }
    }
}
