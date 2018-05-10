package soup.movie.ui.theater.sort;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import soup.movie.R;
import soup.movie.di.ActivityScoped;
import soup.movie.ui.BaseActivity;
import soup.movie.util.RecyclerViewUtil;
import soup.widget.drag.SimpleItemTouchHelperCallback;

@ActivityScoped
public class TheaterSortActivity extends BaseActivity implements TheaterSortContract.View {

    @Inject
    TheaterSortContract.Presenter presenter;

    @BindView(R.id.list)
    RecyclerView recyclerView;

    private TheaterSortListAdapter adapter;

    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater_sort);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(RecyclerViewUtil.createLinearLayoutManager(this));

        presenter.attach(this);
    }

    @Override
    protected void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    @Override
    public void render(@NonNull TheaterSortViewState viewState) {
        adapter = new TheaterSortListAdapter(
                viewState.getSelectedTheaters(), itemTouchHelper::startDrag);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.button_cancel)
    public void onCancelClicked() {
        onBackPressed();
    }

    @OnClick(R.id.button_confirm)
    public void onConfirmClicked() {
        presenter.onConfirmClicked(adapter.getSelectedTheaters());
        onBackPressed();
    }
}
