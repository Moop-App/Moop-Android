package soup.movie.ui.main.settings;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.SharedElementCallback;
import android.transition.TransitionInflater;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import soup.movie.R;
import soup.movie.data.model.Theater;
import soup.movie.ui.main.BaseTabFragment;
import soup.movie.ui.main.settings.SettingsViewState.DoneState;
import soup.movie.ui.theater.sort.TheaterSortActivity;
import timber.log.Timber;

public class SettingsFragment extends BaseTabFragment<SettingsContract.View, SettingsContract.Presenter>
        implements SettingsContract.View {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @BindView(R.id.theater_empty)
    TextView theaterEmpty;

    @BindView(R.id.theater_group)
    ChipGroup theaterGroup;

    @Inject
    SettingsContract.Presenter presenter;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setExitTransition(TransitionInflater.from(getContext())
                .inflateTransition(android.R.transition.explode));
        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                for (String name : names) {
                    View child = theaterGroup.findViewWithTag(name);
                    sharedElements.put(name, child);
                }
            }
        });
        postponeEnterTransition();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_settings;
    }

    @NonNull
    @Override
    protected SettingsContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void render(@NonNull SettingsViewState viewState) {
        Timber.i("render: %s", viewState);
        if (viewState instanceof DoneState) {
            renderInternal((DoneState) viewState);
        } else {
            throw new IllegalStateException("Unknown UI Model");
        }
    }

    private void renderInternal(DoneState state) {
        List<Theater> theaters = state.getTheaterList();
        if (theaters.isEmpty()) {
            theaterEmpty.setVisibility(View.VISIBLE);
            theaterGroup.removeAllViews();
            theaterGroup.setVisibility(View.GONE);
        } else {
            theaterEmpty.setVisibility(View.GONE);
            theaterGroup.removeAllViews();
            theaterGroup.setVisibility(View.VISIBLE);

            for (Theater theater : theaters) {
                Chip theaterChip = (Chip) View.inflate(getContext(), R.layout.chip_cgv, null);
                theaterChip.setChipText(theater.getName());
                theaterChip.setTransitionName(theater.getCode());
                theaterChip.setTag(theater.getCode());
                theaterGroup.addView(theaterChip);
            }
        }
    }

    @OnClick(R.id.theater_edit)
    public void onTheaterEditClicked() {
        Intent intent = new Intent(getContext(), TheaterSortActivity.class);
        startActivity(intent, ActivityOptions
                .makeSceneTransitionAnimation(getActivity(), createTheaterChipPairsForTransition())
                .toBundle());
    }

    private Pair<View,String>[] createTheaterChipPairsForTransition() {
        int childCount = theaterGroup.getChildCount();
        Pair<View, String>[] pairs = new Pair[childCount];
        for (int i = 0; i < childCount; i++) {
            View v = theaterGroup.getChildAt(i);
            pairs[i] = Pair.create(v, v.getTransitionName());
        }
        return pairs;
    }
}
