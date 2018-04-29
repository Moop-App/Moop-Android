package soup.movie.util;

import android.content.Intent;
import android.os.Bundle;
import com.google.gson.Gson;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import soup.movie.data.model.Movie;

public class MovieUtil {
    private MovieUtil() {}

    private static final String KEY_JSON = "json";

    @Nullable
    public static Movie restoreFrom(@NonNull Bundle bundle) {
        return fromJson(bundle.getString(KEY_JSON));
    }

    @Nullable
    public static Movie restoreFrom(@NonNull Intent intent) {
        return fromJson(intent.getStringExtra(KEY_JSON));
    }

    public static void saveTo(@NonNull Bundle bundle, @NonNull Movie movie) {
        bundle.putString(KEY_JSON, toJson(movie));
    }

    public static void saveTo(@NonNull Intent intent, @NonNull Movie movie) {
        intent.putExtra(KEY_JSON, toJson(movie));
    }

    private static String toJson(Movie movie) {
        return new Gson().toJson(movie);
    }

    private static Movie fromJson(String jsonStr) {
        return new Gson().fromJson(jsonStr, Movie.class);
    }

    public static String createShareDescription(Movie movie) {
        return new StringBuilder()
                .append("제목: ")
                .append(movie.getTitle())
                .append("\n개봉일: ")
                .append(movie.getOpenDate())
                .append("\n연령제한: ")
                .append(movie.getAge())
                .toString();
    }
}
