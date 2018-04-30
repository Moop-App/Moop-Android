package soup.movie.ui.theater;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import soup.movie.R;
import soup.movie.di.ActivityScoped;
import soup.movie.ui.BaseActivity;
import soup.movie.util.RecyclerViewUtil;

@ActivityScoped
public class TheaterEditActivity extends BaseActivity implements TheaterEditContract.View {

    @Inject
    TheaterEditContract.Presenter presenter;

    @BindView(R.id.list)
    RecyclerView recyclerView;

    private TheaterEditListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater_edit);
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
    public void render(@NonNull TheaterEditViewState viewState) {
        adapter = new TheaterEditListAdapter(
                viewState.getAllTheaters(),
                viewState.getSelectedTheaters());
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.button_cancel)
    public void onCancelClicked() {
        finish();
    }

    @OnClick(R.id.button_confirm)
    public void onConfirmClicked() {
        presenter.onConfirmClicked(adapter.getSelectedTheaters());
        finish();
    }
}
