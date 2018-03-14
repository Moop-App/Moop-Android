package soup.movie.util;

import java.util.List;

public class ListUtil {

    public static int size(List<?> list) {
        return list == null ? 0 : list.size();
    }
}
