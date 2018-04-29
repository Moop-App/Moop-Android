package soup.movie.ui.main.settings;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import soup.movie.R;
import soup.movie.data.model.TheaterCode;
import soup.movie.di.FragmentScoped;
import soup.movie.ui.main.MainTabFragment;
import soup.movie.ui.main.settings.SettingsViewState.DoneState;
import soup.movie.util.ListUtil;
import timber.log.Timber;

@FragmentScoped
public class SettingsFragment extends MainTabFragment implements SettingsContract.View {

    @BindView(R.id.theater_option)
    TextView theaterOption;

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
        presenter = new SettingsPresenter();
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
            theaterOption.setText("없음");
            theaterOption.setTextColor(Color.RED);
        } else {
            theaterOption.setText(StringUtils.join(ListUtil.toStringArray(theaters, TheaterCode::getName)));
            theaterOption.setTextColor(Color.BLACK);
        }
    }

    @OnClick(R.id.theater_option)
    public void onClick(View view) {
        presenter.onClick(view.getContext());
    }
}
