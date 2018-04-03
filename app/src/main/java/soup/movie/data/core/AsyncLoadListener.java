package soup.movie.data.core;

public interface AsyncLoadListener<T> {
    void onLoaded(T loadedData);
}
