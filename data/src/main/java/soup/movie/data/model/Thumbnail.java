package soup.movie.data.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class Thumbnail {

    @SerializedName("url")
    private String url;

    public Thumbnail() {
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Thumbnail{" +
                "url='" + url + '\'' +
                '}';
    }
}
