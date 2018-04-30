package soup.movie.ui.main.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import soup.movie.R;
import soup.movie.data.model.TheaterCode;
import soup.movie.di.FragmentScoped;
import soup.movie.ui.main.MainTabFragment;
import soup.movie.ui.main.settings.SettingsViewState.DoneState;
import timber.log.Timber;

@FragmentScoped
public class SettingsFragment extends MainTabFragment implements SettingsContract.View {

    @BindView(R.id.theater_empty)
    TextView theaterEmpty;

    @BindView(R.id.theater_group)
    ChipGroup theaterGroup;

    @Inject
    SettingsContract.Presenter presenter;

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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attach(this);
    }

    @Override
    public void onDestroyView() {
        presenter.detach();
        super.onDestroyView();
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

    private void renderInternal(DoneState viewState) {
        List<TheaterCode> theaters = viewState.getTheaterList();
        if (theaters.isEmpty()) {
            theaterEmpty.setVisibility(View.VISIBLE);
            theaterGroup.removeAllViews();
            theaterGroup.setVisibility(View.GONE);
        } else {
            theaterEmpty.setVisibility(View.GONE);
            theaterGroup.removeAllViews();
            theaterGroup.setVisibility(View.VISIBLE);
            for (TheaterCode theater : theaters) {
                Chip theaterChip = new Chip(getContext());
                theaterChip.setText(theater.getName());
                theaterGroup.addView(theaterChip);
            }
        }
    }

    @OnClick(R.id.theater_edit)
    public void onTheaterEditClicked() {
        //TODO
    }
}
