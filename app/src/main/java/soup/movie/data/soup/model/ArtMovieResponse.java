package soup.movie.data.soup.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArtMovieResponse {

    @SerializedName("list")
    private List<Movie> mList;

    public ArtMovieResponse() {
    }

    public List<Movie> getList() {
        return mList;
    }

    @Override
    public String toString() {
        return "ArtMovieResponse{" +
                "mList=" + mList +
                '}';
    }
}
