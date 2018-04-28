package soup.movie.ui.plan;

import android.support.annotation.NonNull;

import soup.movie.ui.BaseContract;

public interface PlanContract {

    interface Presenter extends BaseContract.Presenter<View> {
    }

    interface View extends BaseContract.View {
        void render(@NonNull PlanViewState viewState);
    }
}
