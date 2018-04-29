package soup.movie.ui.main;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationViewHelper;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import soup.movie.R;
import soup.movie.ui.BaseActivity;
import soup.movie.ui.main.MainViewState.NowState;
import soup.movie.ui.main.MainViewState.PlanState;
import soup.movie.ui.main.MainViewState.SettingsState;
import soup.movie.ui.main.now.NowFragment;
import soup.movie.ui.main.plan.PlanFragment;
import soup.movie.ui.main.settings.SettingsFragment;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements MainContract.View {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView navigationView;

    private MainContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initBottomNavigationView();

        presenter = new MainPresenter();
        presenter.attach(this);

        navigationView.setSelectedItemId(R.id.action_now); //default
    }

    private void initBottomNavigationView() {
        BottomNavigationViewHelper.disableShiftMode(navigationView);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            setTitle(item.getTitle());
            presenter.setTabMode(parseToTabMode(item.getItemId()));
            return true;
        });
    }

    @Override
    protected void onDestroy() {
        presenter = null;
        super.onDestroy();
    }

    @Override
    public void render(@NonNull MainViewState viewState) {
        Timber.i("render: %s", viewState);
        if (viewState instanceof NowState) {
            renderInternal((NowState) viewState);
        } else if (viewState instanceof PlanState) {
            renderInternal((PlanState) viewState);
        } else if (viewState instanceof SettingsState) {
            renderInternal((SettingsState) viewState);
        }
    }

    private void renderInternal(@NonNull NowState viewState) {
        commit(R.id.tab_container, NowFragment.newInstance());
    }

    private void renderInternal(@NonNull PlanState viewState) {
        commit(R.id.tab_container, PlanFragment.newInstance());
    }

    private void renderInternal(@NonNull SettingsState viewState) {
        commit(R.id.tab_container, SettingsFragment.newInstance());
    }

    private static @MainContract.TabMode int parseToTabMode(@IdRes int itemId) {
        switch (itemId) {
            case R.id.action_now:
                return MainContract.TAB_MODE_NOW;
            case R.id.action_plan:
                return MainContract.TAB_MODE_PLAN;
            case R.id.action_settings:
                return MainContract.TAB_MODE_SETTINGS;
            default:
                throw new IllegalStateException("Unknown resource ID");
        }
    }
}
