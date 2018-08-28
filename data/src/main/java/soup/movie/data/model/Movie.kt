package soup.movie.data.model

import com.google.gson.annotations.SerializedName

data class Movie(
        val id: String,
        val title: String,
        val thumbnail: String,
        val poster: String,
        val age: String,
        @SerializedName("open_date")
        val openDate: String,
        val egg: String,
        @SerializedName("special_types")
        val specialTypes: List<String>,
        val trailers: List<Trailer>)
