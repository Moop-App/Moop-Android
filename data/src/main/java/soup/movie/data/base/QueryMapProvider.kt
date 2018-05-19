package soup.movie.data.base

@FunctionalInterface
interface QueryMapProvider {

    fun toQueryMap(): Map<String, String>
}
