package soup.movie.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;

import soup.movie.ui.BaseFragment;

public abstract class MainTabFragment extends BaseFragment {

    private static final int INVALID = 0;

    public MainTabFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(getMenuResource() != INVALID);
    }

    @Override
    public final void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        int menuRes = getMenuResource();
        if (menuRes != INVALID) {
            inflater.inflate(menuRes, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    protected @MenuRes int getMenuResource() {
        return INVALID;
    }

    protected final void setTitle(String title) {
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            activity.setTitle(title);
        }
    }
}
