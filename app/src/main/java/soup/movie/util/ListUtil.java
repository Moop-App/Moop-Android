package soup.movie.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.annotations.NonNull;
import soup.movie.util.function.Function;

public class ListUtil {

    public static int size(List<?> list) {
        return list == null ? 0 : list.size();
    }

    public static <T> List<String> toStringList(@NonNull List<T> items,
                                             @NonNull Function<T, String> function) {
        List<String> results = new ArrayList<>(items.size());
        for (T item : items) {
            results.add(function.apply(item));
        }
        return results;
    }

    public static <T> String[] toStringArray(@NonNull List<T> items,
                                         @NonNull Function<T, String> function) {
        final int size = items.size();
        String[] results = new String[size];
        for (int i = 0; i < size; i++) {
            results[i] = function.apply(items.get(i));
        }
        return results;
    }

    public static <T> Set<String> toStringSet(@NonNull List<T> items,
                                              @NonNull Function<T, String> function) {
        Set<String> results = new HashSet<>();
        for (T item : items) {
            results.add(function.apply(item));
        }
        return results;
    }
}
