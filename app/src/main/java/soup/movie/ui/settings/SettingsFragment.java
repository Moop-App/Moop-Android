package soup.movie.ui.settings;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import soup.movie.R;
import soup.movie.data.soup.model.TheaterCode;
import soup.movie.ui.main.MainTabFragment;
import soup.movie.util.ListUtil;
import timber.log.Timber;

public class SettingsFragment extends MainTabFragment implements SettingsContract.View {

    @BindView(R.id.theater_option)
    TextView mTheaterOption;

    private SettingsContract.Presenter mPresenter;

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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);

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
            List<TheaterCode> theaters = ((SettingsUiModel.Data) uiModel).getTheaterList();
            if (theaters.isEmpty()) {
                mTheaterOption.setText("없음");
                mTheaterOption.setTextColor(Color.RED);
            } else {
                mTheaterOption.setText(StringUtils.join(ListUtil.toStringArray(theaters, TheaterCode::getName)));
                mTheaterOption.setTextColor(Color.BLACK);
            }
        } else {
            throw new IllegalStateException("Unknown UI Model");
        }
    }

    @OnClick(R.id.theater_option)
    public void onClick(View view) {
        mPresenter.onClick(view.getContext());
    }
}
