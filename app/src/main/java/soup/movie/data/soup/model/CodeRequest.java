package soup.movie.data.soup.model;

import java.util.HashMap;
import java.util.Map;

import soup.movie.data.core.QueryMapProvider;

public class CodeRequest implements QueryMapProvider {

    public CodeRequest() {
    }

    @Override
    public String toString() {
        return "CodeRequest{}";
    }

    @Override
    public Map<String, String> toQueryMap() {
        return new HashMap<>();
    }
}
