package soup.movie.ui.main;

import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationViewHelper;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import soup.movie.R;
import soup.movie.ui.BaseFragment;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private MainContract.Presenter mPresenter;

    private View mRootView;
    private TextView mSubPanelTitleView;

    private BottomSheetBehavior mBottomSheetBehavior;

    @Nullable
    private BaseFragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter();
        mPresenter.attach(this);

        mRootView = findViewById(R.id.root);
        mSubPanelTitleView = findViewById(R.id.filter_title);

        initBottomNavigationView();
        initBottomSheetView();
    }

    @Override
    protected void onDestroy() {
        mPresenter = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (setSubPanelVisibility(false)) {
            return;
        }
        super.onBackPressed();
    }

    public boolean setSubPanelVisibility(boolean show) {
        int newState = show ? BottomSheetBehavior.STATE_COLLAPSED : BottomSheetBehavior.STATE_HIDDEN;
        BottomSheetBehavior behavior = mBottomSheetBehavior;
        if (behavior != null && behavior.getState() != newState) {
            behavior.setState(newState);
            return true;
        }
        return false;
    }

    public void setSubPanel(String title) {
        TextView subPanelTitleView = mSubPanelTitleView;
        if (subPanelTitleView != null) {
            subPanelTitleView.setText(title);
        }
    }

    @Override
    public void render(MainUiModel uiModel) {
        Timber.i("render: %s", uiModel);
        mCurrentFragment = uiModel.newFragment();
        commit(R.id.tab_container, mCurrentFragment);
    }

    private void commit(@IdRes int containerId, @NonNull Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

    private void initBottomNavigationView() {
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(navigationView);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            Timber.d("Select %s", item.getTitle());
            setTitle(item.getTitle());
            mPresenter.setTabMode(parseToTabMode(item.getItemId()));
            return true;
        });
        navigationView.setSelectedItemId(R.id.action_home); //default
    }

    private void initBottomSheetView() {
        BottomSheetBehavior behavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        behavior.setHideable(true);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        behavior.setPeekHeight(getResources().getDimensionPixelSize(R.dimen.bottom_sheet_peek_height));
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Timber.d("onSlide: %f", slideOffset);
                setScrim(slideOffset);
            }
        });
        mBottomSheetBehavior = behavior;

        setScrim(-1f);
    }

    private void setScrim(float slideOffset) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mRootView.getForeground().setAlpha((int) ((1 + slideOffset) * 75));
        }
    }

    private static @MainContract.TabMode int parseToTabMode(@IdRes int itemId) {
        switch (itemId) {
            case R.id.action_home:
                return MainContract.TAB_MODE_HOME;
            case R.id.action_archive:
                return MainContract.TAB_MODE_ARCHIVE;
            case R.id.action_box_office:
                return MainContract.TAB_MODE_BOX_OFFICE;
            case R.id.action_settings:
                return MainContract.TAB_MODE_SETTINGS;
            default:
                throw new IllegalStateException("Unknown resource ID");
        }
    }
}
