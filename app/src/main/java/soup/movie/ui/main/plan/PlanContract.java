package soup.movie.ui.main.plan;

import android.support.annotation.NonNull;

import soup.movie.ui.BaseContract;

public interface PlanContract {

    interface Presenter extends BaseContract.Presenter<View> {
        void refresh();
    }

    interface View extends BaseContract.View {
        void render(@NonNull PlanViewState viewState);
    }
}
