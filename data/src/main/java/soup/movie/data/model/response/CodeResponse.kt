package soup.movie.data.model.response

import soup.movie.data.model.AreaGroup
import soup.movie.data.model.CodeGroup
import soup.movie.data.model.Theater

data class CodeResponse(
    val cgv: CodeGroup,
    val lotte: CodeGroup,
    val megabox: CodeGroup) {

    fun toAreaGroupList(): List<AreaGroup> {
        return listOf(cgv, lotte, megabox).flatMap { it.list }
    }

    fun toTheaterList(): List<Theater> {
        return toAreaGroupList().flatMap { it.theaterList }
    }
}
