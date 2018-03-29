package soup.movie.data.soup.model;

import com.google.gson.annotations.JsonAdapter;

import java.util.List;

import soup.movie.data.soup.converter.TrailerResponseDeserializer;

@JsonAdapter(TrailerResponseDeserializer.class)
public class TrailerResponse {

    private List<Trailer> trailerList;

    public TrailerResponse() {
    }

    public List<Trailer> getTrailerList() {
        return trailerList;
    }

    public void setTrailerList(List<Trailer> trailerList) {
        this.trailerList = trailerList;
    }

    @Override
    public String toString() {
        return "TrailerResponse{" +
                "trailerList=" + trailerList +
                '}';
    }
}
