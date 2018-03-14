package soup.movie.ui.filter.custom;

import soup.movie.ui.filter.Filter;

public class CalendarFilter<T> implements Filter<T> {
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
