package soup.movie.data.model

import androidx.annotation.Keep

@Keep
data class AreaGroup(
    val area: Area,
    val theaterList: List<Theater>
)
