package soup.movie.data.soup.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CodeResponse {

    @SerializedName("list")
    private List<Area> list;

    public CodeResponse() {
    }

    public List<Area> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "CodeResponse{" +
                "list=" + list +
                '}';
    }
}
