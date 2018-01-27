package soup.movie.data;

import java.util.Map;

@FunctionalInterface
public interface QueryMapProvider {

    Map<String, String> toQueryMap();
}
