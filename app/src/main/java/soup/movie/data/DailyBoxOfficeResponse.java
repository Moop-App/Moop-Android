package soup.movie.data;

import com.google.gson.annotations.SerializedName;

import soup.movie.data.DailyBoxOfficeRequest;
import soup.movie.data.MovieListResult;

public class DailyBoxOfficeResponse {

    @SerializedName("boxOfficeResult")
    private DailyBoxOfficeResult result;

    public DailyBoxOfficeResult getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "DailyBoxOfficeResponse{" +
                "result=" + result +
                '}';
    }
}
