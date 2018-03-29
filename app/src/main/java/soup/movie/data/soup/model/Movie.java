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
    private String openDate;

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
        return openDate;
    }

    public String getEgg() {
        return egg;
    }

    public List<String> getSpecialTypeList() {
        return specialTypeList;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setOpenDate(String open_date) {
        this.openDate = open_date;
    }

    public void setEgg(String egg) {
        this.egg = egg;
    }

    public void setSpecialTypeList(List<String> specialTypeList) {
        this.specialTypeList = specialTypeList;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", age='" + age + '\'' +
                ", openDate='" + openDate + '\'' +
                ", egg='" + egg + '\'' +
                ", specialTypeList=" + specialTypeList +
                '}';
    }
}
