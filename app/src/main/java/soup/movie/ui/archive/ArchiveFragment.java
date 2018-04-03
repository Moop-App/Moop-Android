package soup.movie.ui.archive;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import soup.movie.R;
import soup.movie.ui.main.MainTabFragment;
import timber.log.Timber;

public class ArchiveFragment extends MainTabFragment implements ArchiveContract.View {

    private ArchiveContract.Presenter mPresenter;

    private ArchiveListAdapter mAdapterView;

    public ArchiveFragment() {
    }

    public static ArchiveFragment newInstance() {
        return new ArchiveFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list, container, false);

        Context context = view.getContext();

        ArchiveListAdapter adapterView = new ArchiveListAdapter();
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapterView);
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        mAdapterView = adapterView;

        mPresenter = new ArchivePresenter();
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
        return R.menu.menu_archive;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                showSubPanel(getString(R.string.action_help));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void render(ArchiveUiModel uiModel) {
        Timber.i("render: %s", uiModel);
        if (uiModel instanceof ArchiveUiModel.InProgress) {
            ArchiveListAdapter adapterView = mAdapterView;
            if (adapterView != null) {
                adapterView.updateList(null);
            }
        } else if (uiModel instanceof ArchiveUiModel.Data) {
            ArchiveUiModel.Data data = (ArchiveUiModel.Data)uiModel;
            ArchiveListAdapter adapterView = mAdapterView;
            if (adapterView != null) {
                adapterView.updateList(data.getMovies());
            }
        } else {
            throw new IllegalStateException("Unknown UI Model");
        }
    }
}
