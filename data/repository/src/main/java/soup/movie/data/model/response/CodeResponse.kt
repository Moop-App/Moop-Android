package soup.movie.data.model.response

import soup.movie.data.model.AreaGroup

data class CodeResponse(
    val lastUpdateTime: Long,
    val cgv: List<AreaGroup>,
    val lotte: List<AreaGroup>,
    val megabox: List<AreaGroup>
)
