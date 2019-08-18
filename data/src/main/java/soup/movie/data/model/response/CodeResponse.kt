package soup.movie.data.model.response

import androidx.annotation.Keep
import soup.movie.data.model.CodeGroup

@Keep
data class CodeResponse(
    val cgv: CodeGroup,
    val lotte: CodeGroup,
    val megabox: CodeGroup
)
