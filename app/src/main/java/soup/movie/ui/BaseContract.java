package soup.movie.ui;

public interface BaseContract {

    interface Presenter<T extends View> {
        void attach(T view);
        void detach();
    }

    interface View {
    }
}
