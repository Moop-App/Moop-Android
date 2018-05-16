package soup.movie.data.model

import com.google.gson.annotations.SerializedName

data class CodeResponse(
        @SerializedName("cgv") val cgv: CodeGroup,
        @SerializedName("lotte") val lotte: CodeGroup,
        @SerializedName("megabox") val megabox: CodeGroup
)

data class CodeGroup(
        @SerializedName("lastUpdateTime") val lastUpdateTime: Int,
        @SerializedName("list") val list: List<AreaGroup>
)

data class AreaGroup(
        @SerializedName("area") val area: Area,
        @SerializedName("theaterList") val theaterList: List<Theater>
)

data class Area(
        @SerializedName("code") val code: String,
        @SerializedName("name") val name: String
)

data class Theater(
        @SerializedName("code") val code: String,
        @SerializedName("name") val name: String
)