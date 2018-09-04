package soup.movie.util.log

import android.util.Log
import com.crashlytics.android.Crashlytics
import soup.movie.util.log.LogConstants.RENDER_MESSAGE_PREFIX
import timber.log.Timber

class CrashlyticsTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO
                || message.startsWith(RENDER_MESSAGE_PREFIX).not()) {
            return
        }
        when {
            t != null -> Crashlytics.logException(t)
            tag != null -> Crashlytics.log("$tag: $message")
            else -> Crashlytics.log(message)
        }
    }
}