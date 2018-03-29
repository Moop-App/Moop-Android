package soup.movie.data.soup.converter;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import soup.movie.data.soup.model.Trailer;
import soup.movie.data.soup.model.TrailerResponse;

public class TrailerResponseDeserializer implements JsonDeserializer<TrailerResponse> {

  @Override
  public TrailerResponse deserialize(JsonElement json, Type typeOfT,
                                     JsonDeserializationContext context) throws JsonParseException {
    ArrayList<Trailer> trailers = new ArrayList<>();
    if (json.isJsonArray()) {
      Gson gson = new Gson();
      for (JsonElement jsonElement : json.getAsJsonArray()) {
        trailers.add(gson.fromJson(jsonElement, Trailer.class));
      }
    }
    TrailerResponse response = new TrailerResponse();
    response.setTrailerList(trailers);
    return response;
  }
}
