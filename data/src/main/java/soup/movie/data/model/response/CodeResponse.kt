package soup.movie.data.model.response

import androidx.annotation.Keep
import soup.movie.data.model.AreaGroup

@Keep
data class CodeResponse(
    val lastUpdateTime: Long,
    val cgv: List<AreaGroup>,
    val lotte: List<AreaGroup>,
    val megabox: List<AreaGroup>
)
