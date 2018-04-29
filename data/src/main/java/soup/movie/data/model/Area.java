package soup.movie.data.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Keep
public class Area {

    @SerializedName("area")
    private AreaCode area;

    @SerializedName("theaterList")
    private List<TheaterCode> theaterList;

    public Area() {
    }

    public AreaCode getArea() {
        return area;
    }

    public List<TheaterCode> getTheaterList() {
        return theaterList;
    }

    @Override
    public String toString() {
        return "Area{" +
                "area=" + area +
                ", theaterList=" + theaterList +
                '}';
    }
}
