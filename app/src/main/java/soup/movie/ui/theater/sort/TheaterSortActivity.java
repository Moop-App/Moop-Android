package soup.movie.ui.theater.sort;

import android.app.SharedElementCallback;
import android.content.Context;
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
import butterknife.OnClick;
import soup.movie.R;
import soup.movie.ui.BaseActivity;
import soup.movie.ui.theater.edit.TheaterEditActivity;
import soup.widget.drag.ItemTouchHelperAdapter;
import soup.widget.drag.SimpleItemTouchHelperCallback;

import static soup.movie.util.RecyclerViewUtil.verticalLinearLayoutManager;

public class TheaterSortActivity extends BaseActivity<TheaterSortContract.View, TheaterSortContract.Presenter>
        implements TheaterSortContract.View {

    @Inject
    TheaterSortContract.Presenter presenter;

    @BindView(R.id.list)
    RecyclerView listView;

    private TheaterSortListAdapter listAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_theater_sort;
    }

    @NonNull
    @Override
    protected TheaterSortContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                for (String name : names) {
                    View child = listView.findViewWithTag(name);
                    sharedElements.put(name, child);
                }
            }
        });
    }

    @Override
    protected void initViewState(@NonNull Context ctx) {
        super.initViewState(ctx);
        listView.setLayoutManager(verticalLinearLayoutManager(this));
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
                listAdapter.onItemMove(fromPosition, toPosition);
            }

            @Override
            public void onItemDismiss(int position) {
                listAdapter.onItemDismiss(position);
            }
        };
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapterDelegate);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(listView);

        listAdapter = new TheaterSortListAdapter(
                viewState.getSelectedTheaters(), itemTouchHelper::startDrag);
        listView.setAdapter(listAdapter);
        startPostponedEnterTransition();
    }

    @OnClick(R.id.button_cancel)
    public void onCancelClicked() {
        onBackPressed();
    }

    @OnClick(R.id.button_confirm)
    public void onConfirmClicked() {
        presenter.onConfirmClicked(listAdapter.getSelectedTheaters());
        onBackPressed();
    }
}
