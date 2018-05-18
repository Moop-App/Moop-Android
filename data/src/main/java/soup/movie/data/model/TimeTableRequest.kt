package soup.movie.data.model

import java.util.HashMap

import soup.movie.data.base.QueryMapProvider

class TimeTableRequest(val theaterCode: String, val movieCode: String) : QueryMapProvider {

    override fun toString(): String {
        return "TimeTableRequest{" +
                "theaterCode='" + theaterCode + '\''.toString() +
                ", movieCode='" + movieCode + '\''.toString() +
                '}'.toString()
    }

    override fun toQueryMap(): Map<String, String> {
        return HashMap()
    }
}
