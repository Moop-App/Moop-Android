package soup.movie.data.request

import soup.movie.data.base.QueryMapProvider

object PlanMovieRequest : QueryMapProvider {

    override fun toQueryMap(): Map<String, String> {
        return HashMap()
    }
}
