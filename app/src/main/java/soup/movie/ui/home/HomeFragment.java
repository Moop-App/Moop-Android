package soup.movie.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
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

public class HomeFragment extends MainTabFragment implements HomeContract.View {

    private HomeContract.Presenter mPresenter;

    private HomeListAdapter mAdapterView;

    @BindView(R.id.title)
    TextView mTitleView;

    @BindView(R.id.list)
    RecyclerView mListView;

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
        View view = inflater.inflate(R.layout.list_with_title, container, false);
        ButterKnife.bind(this, view);

        Context context = view.getContext();

        HomeListAdapter adapterView = new HomeListAdapter(getActivity());
        RecyclerView recyclerView = mListView;
        recyclerView.setLayoutManager(new SnappyLinearLayoutManager(context).horizontally());
        recyclerView.setAdapter(adapterView);
        recyclerView.setItemAnimator(new SlideInRightAnimator());
        recyclerView.getItemAnimator().setAddDuration(200);
        recyclerView.getItemAnimator().setRemoveDuration(200);
        mAdapterView = adapterView;

        mPresenter = new HomePresenter();
        mPresenter.attach(this);
        mPresenter.requestMovieList(HomeContract.Presenter.Type.NOW);

        return view;
    }

    @Override
    public void onDestroyView() {
        mPresenter.detach();
        super.onDestroyView();
    }

    @Override
    protected int getMenuResource() {
        return R.menu.menu_home;
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
    public void render(HomeUiModel uiModel) {
        Timber.i("render: %s", uiModel);
        if (uiModel instanceof HomeUiModel.InProgress) {
            updateMovieList(null);
        } else if (uiModel instanceof HomeUiModel.Data) {
            HomeUiModel.Data data = (HomeUiModel.Data)uiModel;
            mTitleView.setText(data.getTitle());
            updateMovieList(data.getMovies());
        } else {
            throw new IllegalStateException("Unknown UI Model");
        }
    }

    private void updateMovieList(@Nullable List<Movie> movieList) {
        HomeListAdapter adapterView = mAdapterView;
        if (adapterView != null) {
            adapterView.updateList(movieList);
        }
    }
}
