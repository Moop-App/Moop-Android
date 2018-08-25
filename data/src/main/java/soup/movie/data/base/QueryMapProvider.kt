package soup.movie.data.base

interface QueryMapProvider {

    fun toQueryMap(): Map<String, String>
}
