package soup.movie.data.soup.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movie {

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("thumbnail")
    private String thumbnailUrl;

    @SerializedName("age")
    private String age;

    @SerializedName("open_date")
    private String open_date;

    @SerializedName("egg")
    private String egg;

    @SerializedName("special_types")
    private List<String> specialTypeList;

    public Movie() {
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getAge() {
        return age;
    }

    public String getOpenDate() {
        return open_date;
    }

    public String getEgg() {
        return egg;
    }

    public List<String> getSpecialTypeList() {
        return specialTypeList;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", age='" + age + '\'' +
                ", open_date='" + open_date + '\'' +
                ", egg='" + egg + '\'' +
                ", specialTypeList=" + specialTypeList +
                '}';
    }
}
