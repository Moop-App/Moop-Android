package soup.movie.data.kobis.model;

import com.google.gson.annotations.SerializedName;

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
