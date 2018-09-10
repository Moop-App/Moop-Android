package soup.movie.data.model.response

import soup.movie.data.model.CodeGroup

data class CodeResponse(
        val cgv: CodeGroup,
        val lotte: CodeGroup,
        val megabox: CodeGroup)
