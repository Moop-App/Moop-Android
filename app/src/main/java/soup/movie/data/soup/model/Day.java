package soup.movie.data.soup.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Day {

    @SerializedName("date")
    private String date;

    @SerializedName("timeList")
    private List<String> timeList;

    public Day() {
    }

    public String getDate() {
        return date;
    }

    public List<String> getTimeList() {
        return timeList;
    }

    @Override
    public String toString() {
        return "Day{" +
                "date='" + date + '\'' +
                ", timeList=" + timeList +
                '}';
    }
}
