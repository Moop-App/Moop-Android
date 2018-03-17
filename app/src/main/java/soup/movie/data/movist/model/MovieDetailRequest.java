package soup.movie.data.movist.model;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import soup.movie.data.core.QueryMapProvider;

public class MovieDetailRequest implements QueryMapProvider {

    private final String mId;

    public MovieDetailRequest(@NonNull String id) {
        mId = id;
    }

    @Override
    public String toString() {
        return "MovieDetailRequest{" +
                "mId='" + mId + '\'' +
                '}';
    }

    @Override
    public Map<String, String> toQueryMap() {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("id", mId);
        return queryMap;
    }
}
