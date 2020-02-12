package soup.movie.install

interface InAppUpdateManager {

    suspend fun getAvailableVersionCode(): Int

    companion object {

        const val UNKNOWN_VERSION_CODE = 0
    }
}
