package soup.movie

import android.content.Context
import com.facebook.stetho.Stetho
import timber.log.Timber

object BuildType {

    fun init(context: Context) {
        Timber.plant(Timber.DebugTree())
        Stetho.initializeWithDefaults(context)
    }
}
