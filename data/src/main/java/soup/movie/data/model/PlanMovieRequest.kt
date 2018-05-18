package soup.movie.data.model

import java.util.HashMap

import soup.movie.data.base.QueryMapProvider

class PlanMovieRequest : QueryMapProvider {

    override fun toString(): String {
        return "PlanMovieRequest{}"
    }

    override fun toQueryMap(): Map<String, String> {
        return HashMap()
    }
}
