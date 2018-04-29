package soup.movie.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlanMovieResponse {

    @SerializedName("list")
    private List<Movie> list;

    public PlanMovieResponse() {
    }

    public List<Movie> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "PlanMovieResponse{" +
                "list=" + list +
                '}';
    }
}
