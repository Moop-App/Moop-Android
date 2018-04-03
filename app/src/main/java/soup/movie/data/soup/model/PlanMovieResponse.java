package soup.movie.data.soup.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlanMovieResponse {

    @SerializedName("list")
    private List<Movie> mList;

    public PlanMovieResponse() {
    }

    public List<Movie> getList() {
        return mList;
    }

    @Override
    public String toString() {
        return "PlanMovieResponse{" +
                "mList=" + mList +
                '}';
    }
}
