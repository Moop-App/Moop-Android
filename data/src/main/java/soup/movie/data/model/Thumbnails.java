package soup.movie.data.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class Thumbnails {

    @SerializedName("default")
    private Thumbnail low;

    @SerializedName("medium")
    private Thumbnail medium;

    @SerializedName("high")
    private Thumbnail high;

    public Thumbnails() {
    }

    public Thumbnail getLow() {
        return low;
    }

    public Thumbnail getMedium() {
        return medium;
    }

    public Thumbnail getHigh() {
        return high;
    }

    @Override
    public String toString() {
        return "Thumbnails{" +
                "low=" + low +
                ", medium=" + medium +
                ", high=" + high +
                '}';
    }
}
