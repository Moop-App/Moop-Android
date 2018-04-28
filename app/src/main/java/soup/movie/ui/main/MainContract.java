package soup.movie.ui.main;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import soup.movie.ui.BaseContract;

class MainContract {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TAB_MODE_BOX_OFFICE, TAB_MODE_HOME, TAB_MODE_SETTINGS})
    @interface TabMode {}

    static final int TAB_MODE_BOX_OFFICE = 1;
    static final int TAB_MODE_HOME = 2;
    static final int TAB_MODE_SETTINGS = 4;

    interface Presenter extends BaseContract.Presenter<View> {
        void setTabMode(@TabMode int mode);
    }

    interface View extends BaseContract.View {
        void render(@NonNull MainUiModel uiModel);
    }
}
