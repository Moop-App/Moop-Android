package soup.movie.ui.main;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import soup.movie.R;
import soup.movie.ui.archive.ArchiveFragment;
import soup.movie.ui.boxoffice.BoxOfficeFragment;
import soup.movie.ui.home.HomeFragment;
import soup.movie.ui.preview.MoviePreviewFragment;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter();
        mPresenter.attach(this);

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            Timber.d("Select %s", item.getTitle());
            setTitle(item.getTitle());
            mPresenter.setTabMode(parseToTabMode(item.getItemId()));
            return true;
        });
        navigationView.setSelectedItemId(R.id.action_home); //default
    }

    @Override
    protected void onDestroy() {
        mPresenter = null;
        super.onDestroy();
    }

    @Override
    public void render(MainUiModel uiModel) {
        Timber.i("render: %s", uiModel);
        if (uiModel instanceof MainUiModel.BoxOffice) {
            commit(R.id.container, BoxOfficeFragment.newInstance());
        } else if (uiModel instanceof MainUiModel.Home) {
            commit(R.id.container, HomeFragment.newInstance());
        } else if (uiModel instanceof MainUiModel.Archive) {
            commit(R.id.container, ArchiveFragment.newInstance());
        } else {
            throw new IllegalStateException("Unknown UI Model");
        }
    }

    private void commit(@IdRes int containerId, @NonNull Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment, fragment.getClass().getSimpleName())
                .commit();
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
