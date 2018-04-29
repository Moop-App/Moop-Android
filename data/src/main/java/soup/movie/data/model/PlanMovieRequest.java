package soup.movie.data.model;

import java.util.HashMap;
import java.util.Map;

import soup.movie.data.base.QueryMapProvider;

public class PlanMovieRequest implements QueryMapProvider {

    public PlanMovieRequest() {
    }

    @Override
    public String toString() {
        return "PlanMovieRequest{}";
    }

    @Override
    public Map<String, String> toQueryMap() {
        return new HashMap<>();
    }
}
