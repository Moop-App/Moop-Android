package soup.movie

import android.content.Context
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import soup.movie.util.log.CrashlyticsTree
import timber.log.Timber

object BuildType {

    fun init(context: Context) {
        Timber.plant(CrashlyticsTree())
        initCrashlytics(context)
    }

    private fun initCrashlytics(context: Context) {
        val core = CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build()
        Fabric.with(context, Crashlytics.Builder().core(core).build())
    }
}
