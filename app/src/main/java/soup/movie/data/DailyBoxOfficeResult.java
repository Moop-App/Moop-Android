package soup.movie.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DailyBoxOfficeResult {

    @SerializedName("boxofficeType")
    private String boxOfficeType;

    @SerializedName("showRange")
    private String showRange;

    @SerializedName("dailyBoxOfficeList")
    private List<BoxOfficeMovie> dailyBoxOfficeList;

    public String getBoxOfficeType() {
        return boxOfficeType;
    }

    public String getShowRange() {
        return showRange;
    }

    public List<BoxOfficeMovie> getDailyBoxOfficeList() {
        return dailyBoxOfficeList;
    }

    @Override
    public String toString() {
        return "DailyBoxOfficeResult{" +
                "boxOfficeType='" + boxOfficeType + '\'' +
                ", showRange='" + showRange + '\'' +
                ", dailyBoxOfficeList=" + dailyBoxOfficeList +
                '}';
    }
}
