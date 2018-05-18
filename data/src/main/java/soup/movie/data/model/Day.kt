package soup.movie.data.model

import com.google.gson.annotations.SerializedName

data class Day(
    @SerializedName("date") val date: String,
    @SerializedName("timeList") val timeList: List<String>
)