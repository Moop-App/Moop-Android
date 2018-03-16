package soup.movie.data.kobis.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeeklyBoxOfficeResult {

    @SerializedName("boxofficeType")
    private String boxOfficeType;

    @SerializedName("showRange")
    private String showRange;

    @SerializedName("yearWeekTime")
    private String yearWeekTime;

    @SerializedName("weeklyBoxOfficeList")
    private List<BoxOfficeMovie> weeklyBoxOfficeList;

    public String getBoxOfficeType() {
        return boxOfficeType;
    }

    public String getShowRange() {
        return showRange;
    }

    public String getYearWeekTime() {
        return yearWeekTime;
    }

    public List<BoxOfficeMovie> getWeeklyBoxOfficeList() {
        return weeklyBoxOfficeList;
    }

    @Override
    public String toString() {
        return "WeeklyBoxOfficeResult{" +
                "boxOfficeType='" + boxOfficeType + '\'' +
                ", showRange='" + showRange + '\'' +
                ", yearWeekTime='" + yearWeekTime + '\'' +
                ", weeklyBoxOfficeList=" + weeklyBoxOfficeList +
                '}';
    }
}
