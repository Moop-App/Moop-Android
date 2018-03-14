package soup.movie.ui.filter;

public class MultipleChoiceFilter<T> implements Filter<T> {

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
