package soup.movie.ui.home;

import soup.movie.ui.BaseContract;

public interface HomeContract {

    interface Presenter extends BaseContract.Presenter<View> {

        enum Type {
            NOW("무비차트"), ART("아트하우스"), PLAN("개봉예정작");

            private String mName;

            Type(String name) {
                mName = name;
            }

            public String value() {
                return mName;
            }
        }

        void requestMovieList(Type type);
    }

    interface View extends BaseContract.View {
        void render(HomeUiModel uiModel);
    }
}
