package soup.movie.config

interface Config {
    fun fetchAndActivate(onComplete: () -> Unit)
    val allowToRunLegacyWorker: Boolean
}
