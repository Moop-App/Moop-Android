package soup.movie.data.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class AreaCode implements CodeAndName {

    @SerializedName("code")
    private String code;

    @SerializedName("name")
    private String name;

    public AreaCode() {
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "AreaCode{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
