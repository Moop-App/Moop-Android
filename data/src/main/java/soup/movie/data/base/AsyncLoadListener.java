package soup.movie.data.base;

public interface AsyncLoadListener<T> {
    void onLoaded(T loadedData);
}
