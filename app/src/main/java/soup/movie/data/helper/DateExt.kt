package soup.movie.data.helper

import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId

fun today(): LocalDate = LocalDate.now(ZoneId.of("Asia/Seoul"))
