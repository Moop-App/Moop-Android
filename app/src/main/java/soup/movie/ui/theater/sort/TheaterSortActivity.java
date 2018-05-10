package soup.movie.ui.theater.sort;

import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import soup.movie.R;
import soup.movie.di.ActivityScoped;
import soup.movie.ui.BaseActivity;
import soup.movie.ui.theater.edit.TheaterEditActivity;
import soup.movie.util.RecyclerViewUtil;
import soup.widget.drag.ItemTouchHelperAdapter;
import soup.widget.drag.SimpleItemTouchHelperCallback;

@ActivityScoped
public class TheaterSortActivity extends BaseActivity implements TheaterSortContract.View {

    @Inject
    TheaterSortContract.Presenter presenter;

    @BindView(R.id.list)
    RecyclerView recyclerView;

    private TheaterSortListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater_sort);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(RecyclerViewUtil.createLinearLayoutManager(this));

        postponeEnterTransition();
        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                for (String name : names) {
                    View child = recyclerView.findViewWithTag(name);
                    sharedElements.put(name, child);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.attach(this);
    }

    @Override
    protected void onPause() {
        presenter.detach();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                startActivity(new Intent(this, TheaterEditActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void render(@NonNull TheaterSortViewState viewState) {
        ItemTouchHelperAdapter adapterDelegate = new ItemTouchHelperAdapter() {
            @Override
            public void onItemMove(int fromPosition, int toPosition) {
                adapter.onItemMove(fromPosition, toPosition);
            }

            @Override
            public void onItemDismiss(int position) {
                adapter.onItemDismiss(position);
            }
        };
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapterDelegate);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter = new TheaterSortListAdapter(
                viewState.getSelectedTheaters(), itemTouchHelper::startDrag);
        recyclerView.setAdapter(adapter);
        startPostponedEnterTransition();
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
