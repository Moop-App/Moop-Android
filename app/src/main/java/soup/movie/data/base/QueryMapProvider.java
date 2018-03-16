package soup.movie.data.base;

import java.util.Map;

@FunctionalInterface
public interface QueryMapProvider {

    Map<String, String> toQueryMap();
}
