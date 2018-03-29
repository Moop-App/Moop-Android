package soup.movie.data.soup.model;

import java.util.HashMap;
import java.util.Map;

import soup.movie.data.core.QueryMapProvider;

public class TimeTableRequest implements QueryMapProvider {

    private String areaCode;
    private String theaterCode;
    private String movieCode;

    public TimeTableRequest(String areaCode, String theaterCode, String movieCode) {
        this.areaCode = areaCode;
        this.theaterCode = theaterCode;
        this.movieCode = movieCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getTheaterCode() {
        return theaterCode;
    }

    public String getMovieCode() {
        return movieCode;
    }

    @Override
    public String toString() {
        return "TimeTableRequest{}";
    }

    @Override
    public Map<String, String> toQueryMap() {
        return new HashMap<>();
    }
}
