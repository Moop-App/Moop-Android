package soup.movie.data.soup.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArtMovieResponse {

    @SerializedName("list")
    private List<Movie> list;

    public ArtMovieResponse() {
    }

    public List<Movie> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "ArtMovieResponse{" +
                "list=" + list +
                '}';
    }
}
