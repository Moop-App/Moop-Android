package soup.movie.data.model

import soup.movie.data.base.QueryMapProvider
import java.util.*

object NowMovieRequest : QueryMapProvider {

    override fun toQueryMap(): Map<String, String> {
        return HashMap()
    }
}
