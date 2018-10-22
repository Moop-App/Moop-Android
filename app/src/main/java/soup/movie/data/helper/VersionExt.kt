package soup.movie.data.helper

import soup.movie.BuildConfig
import soup.movie.data.model.Version

object VersionHelper {

    fun currentVersion(): Version = Version(
            BuildConfig.VERSION_CODE,
            BuildConfig.VERSION_NAME)
}
