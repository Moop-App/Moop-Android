package soup.movie

import android.content.Context
import timber.log.Timber

object BuildType {

    fun init(context: Context) {
        Timber.plant(CrashlyticsTree())
    }
}
