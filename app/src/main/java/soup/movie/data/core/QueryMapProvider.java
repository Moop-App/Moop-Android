package soup.movie.data.core;

import java.util.Map;

@FunctionalInterface
public interface QueryMapProvider {

    Map<String, String> toQueryMap();
}
