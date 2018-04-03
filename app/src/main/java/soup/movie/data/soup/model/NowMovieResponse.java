package soup.movie.data.soup.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NowMovieResponse {

    @SerializedName("list")
    private List<Movie> mList;

    public NowMovieResponse() {
    }

    public List<Movie> getList() {
        return mList;
    }

    @Override
    public String toString() {
        return "NowMovieResponse{" +
                "mList=" + mList +
                '}';
    }
}
