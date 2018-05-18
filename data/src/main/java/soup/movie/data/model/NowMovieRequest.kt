package soup.movie.data.model

import java.util.HashMap

import soup.movie.data.base.QueryMapProvider

class NowMovieRequest : QueryMapProvider {

    override fun toString(): String {
        return "NowMovieRequest{}"
    }

    override fun toQueryMap(): Map<String, String> {
        return HashMap()
    }
}
