package soup.movie.data.utils;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringDef;
import android.text.TextUtils;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import soup.movie.data.soup.model.Movie;

public class MovieUtil {
    private MovieUtil() {}

    @StringDef({
            Key.ID,
            Key.TITLE,
            Key.AGE,
            Key.EGG,
            Key.OPEN_DATE,
            Key.THUMBNAIL
    })
    private @interface Key {
        String ID = "id";
        String TITLE = "title";
        String AGE = "age";
        String EGG = "egg";
        String OPEN_DATE = "openDate";
        String THUMBNAIL = "thumbnail";
    }

    @Nullable
    public static Movie restoreFrom(@NonNull Bundle bundle) {
        Movie movie = new Movie();
        movie.setId(bundle.getString(Key.ID));
        movie.setTitle(bundle.getString(Key.TITLE));
        movie.setAge(bundle.getString(Key.AGE));
        movie.setEgg(bundle.getString(Key.EGG));
        movie.setOpenDate(bundle.getString(Key.OPEN_DATE));
        movie.setThumbnailUrl(bundle.getString(Key.THUMBNAIL));
        return TextUtils.isEmpty(movie.getId()) ? null : movie;
    }

    @Nullable
    public static Movie restoreFrom(@NonNull Intent intent) {
        Movie movie = new Movie();
        movie.setId(intent.getStringExtra(Key.ID));
        movie.setTitle(intent.getStringExtra(Key.TITLE));
        movie.setAge(intent.getStringExtra(Key.AGE));
        movie.setEgg(intent.getStringExtra(Key.EGG));
        movie.setOpenDate(intent.getStringExtra(Key.OPEN_DATE));
        movie.setThumbnailUrl(intent.getStringExtra(Key.THUMBNAIL));
        return TextUtils.isEmpty(movie.getId()) ? null : movie;
    }

    public static void saveTo(@NonNull Bundle bundle, @NonNull Movie movie) {
        bundle.putString(Key.ID, movie.getId());
        bundle.putString(Key.TITLE, movie.getTitle());
        bundle.putString(Key.AGE, movie.getAge());
        bundle.putString(Key.EGG, movie.getEgg());
        bundle.putString(Key.OPEN_DATE, movie.getOpenDate());
        bundle.putString(Key.THUMBNAIL, movie.getThumbnailUrl());
    }

    public static void saveTo(@NonNull Intent intent, @NonNull Movie movie) {
        intent.putExtra(Key.ID, movie.getId());
        intent.putExtra(Key.TITLE, movie.getTitle());
        intent.putExtra(Key.AGE, movie.getAge());
        intent.putExtra(Key.EGG, movie.getEgg());
        intent.putExtra(Key.OPEN_DATE, movie.getOpenDate());
        intent.putExtra(Key.THUMBNAIL, movie.getThumbnailUrl());
    }
}
