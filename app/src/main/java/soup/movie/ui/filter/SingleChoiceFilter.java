package soup.movie.ui.filter;

import android.support.annotation.NonNull;

import java.util.List;

public class SingleChoiceFilter<T> implements Filter<T> {

    public SingleChoiceFilter(@NonNull List<T> filterItems) {
        this(filterItems, 0);
    }

    public SingleChoiceFilter(@NonNull List<T> filterItems, int defaultIndex) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean reset() {
        return false;
    }

    @Override
    public boolean set(T filterItem) {
        return false;
    }

    @Override
    public T get() {
        return null;
    }
}
