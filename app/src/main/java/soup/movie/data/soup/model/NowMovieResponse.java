package soup.movie.data.soup.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NowMovieResponse {

    @SerializedName("list")
    private List<Movie> list;

    public NowMovieResponse() {
    }

    public List<Movie> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "NowMovieResponse{" +
                "list=" + list +
                '}';
    }
}
