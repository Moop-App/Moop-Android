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
        val trailers: List<Trailer>) {

        fun getSimpleAgeLabel() : String = when (age) {
                "전체 관람가" -> "전체"
                "12세 관람가" -> "12"
                "15세 관람가" -> "15"
                "청소년관람불가" -> "청불"
                else -> "미정"
        }
}
