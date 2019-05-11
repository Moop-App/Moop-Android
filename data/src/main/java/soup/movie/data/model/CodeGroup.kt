package soup.movie.data.model

import androidx.annotation.Keep

@Keep
data class CodeGroup(
    val lastUpdateTime: Int,
    val list: List<AreaGroup>
)
