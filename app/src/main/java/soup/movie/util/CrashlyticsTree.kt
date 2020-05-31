package soup.movie.util

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class CrashlyticsTree : Timber.Tree() {

    private val crashlytics = FirebaseCrashlytics.getInstance()

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return
        }
        when {
            t != null -> crashlytics.recordException(t)
            tag != null -> crashlytics.log("$tag: $message")
            else -> crashlytics.log(message)
        }
    }
}
