package soup.movie.data.model

import java.util.HashMap

import soup.movie.data.base.QueryMapProvider

class CodeRequest : QueryMapProvider {

    override fun toString(): String {
        return "CodeRequest{}"
    }

    override fun toQueryMap(): Map<String, String> {
        return HashMap()
    }
}
