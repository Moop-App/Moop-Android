package soup.movie.ui.main;

import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import soup.movie.R;
import soup.movie.ui.archive.ArchiveFragment;
import soup.movie.ui.boxoffice.BoxOfficeFragment;
import soup.movie.ui.home.HomeFragment;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private MainContract.Presenter mPresenter;

    private View mRootView;
    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter();
        mPresenter.attach(this);

        mRootView = findViewById(R.id.root);

        initBottomNavigationView();
        initBottomSheetView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                BottomSheetBehavior behavior = mBottomSheetBehavior;
                if (behavior != null) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mPresenter = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        BottomSheetBehavior behavior = mBottomSheetBehavior;
        if (behavior != null && behavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void render(MainUiModel uiModel) {
        Timber.i("render: %s", uiModel);
        if (uiModel instanceof MainUiModel.BoxOffice) {
            commit(R.id.tab_container, BoxOfficeFragment.newInstance());
        } else if (uiModel instanceof MainUiModel.Home) {
            commit(R.id.tab_container, HomeFragment.newInstance());
        } else if (uiModel instanceof MainUiModel.Archive) {
            commit(R.id.tab_container, ArchiveFragment.newInstance());
        } else {
            throw new IllegalStateException("Unknown UI Model");
        }
    }

    private void commit(@IdRes int containerId, @NonNull Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

    private void initBottomNavigationView() {
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
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
            default:
                throw new IllegalStateException("Unknown resource ID");
        }
    }
}
