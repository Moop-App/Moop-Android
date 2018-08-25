package soup.movie.data.request

import soup.movie.data.base.QueryMapProvider

data class TimeTableRequest(
        val theaterCode: String,
        val movieCode: String) : QueryMapProvider {

    override fun toQueryMap(): Map<String, String> {
        return HashMap()
    }
}
