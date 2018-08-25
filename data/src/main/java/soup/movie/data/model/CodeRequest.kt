package soup.movie.data.model

import java.util.HashMap

import soup.movie.data.base.QueryMapProvider

object CodeRequest : QueryMapProvider {

    override fun toQueryMap(): Map<String, String> {
        return HashMap()
    }
}
