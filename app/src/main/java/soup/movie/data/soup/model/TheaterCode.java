package soup.movie.data.soup.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class TheaterCode implements CodeAndName {

    @SerializedName("code")
    private String code;

    @SerializedName("name")
    private String name;

    public TheaterCode() {
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "TheaterCode{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
