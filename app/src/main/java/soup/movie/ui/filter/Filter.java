package soup.movie.ui.filter;

public interface Filter<T> {

    String getName();

    boolean reset();

    boolean set(T filterItem);

    T get();
}
