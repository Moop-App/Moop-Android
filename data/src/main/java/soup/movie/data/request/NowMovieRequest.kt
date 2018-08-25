package soup.movie.data.request

import soup.movie.data.base.QueryMapProvider

object NowMovieRequest : QueryMapProvider {

    override fun toQueryMap(): Map<String, String> {
        return HashMap()
    }
}
