package soup.movie.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CodeGroup {

    @SerializedName("list")
    private List<Area> list;

    public CodeGroup() {
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
