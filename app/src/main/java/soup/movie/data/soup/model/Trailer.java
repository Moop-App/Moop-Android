package soup.movie.data.soup.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class Trailer {

    @SerializedName("author")
    private String author;

    @SerializedName("title")
    private String title;

    @SerializedName("youtube_id")
    private String id;

    public Trailer() {
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
