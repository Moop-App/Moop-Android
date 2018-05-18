package soup.movie.data.model

import com.google.gson.annotations.SerializedName

data class Movie(
        @SerializedName("id") val id: String,
        @SerializedName("title") val title: String,
        @SerializedName("thumbnail") val thumbnail: String,
        @SerializedName("poster") val poster: String,
        @SerializedName("age") val age: String,
        @SerializedName("open_date") val openDate: String,
        @SerializedName("egg") val egg: String,
        @SerializedName("special_types") val specialTypes: List<String>,
        @SerializedName("trailers") val trailers: List<Trailer>
)