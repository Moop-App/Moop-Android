package soup.movie.ui.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import soup.movie.R;
import soup.movie.ui.main.MainTabFragment;
import timber.log.Timber;

public class SettingsFragment extends MainTabFragment implements SettingsContract.View {

    private SettingsContract.Presenter mPresenter;

    private SettingsListAdapter mAdapterView;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
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

        SettingsListAdapter adapterView = new SettingsListAdapter();
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapterView);
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        mAdapterView = adapterView;

        mPresenter = new SettingsPresenter();
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
        return 0;
    }

    @Override
    public void render(SettingsUiModel uiModel) {
        Timber.i("render: %s", uiModel);
        if (uiModel instanceof SettingsUiModel.InProgress) {
        } else if (uiModel instanceof SettingsUiModel.Data) {
        } else {
            throw new IllegalStateException("Unknown UI Model");
        }
    }
}
