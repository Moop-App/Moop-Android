package soup.movie.install

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.ktx.requestAppUpdateInfo

class InAppUpdateManagerImpl(context: Context) : InAppUpdateManager {

    private val appUpdateManager = AppUpdateManagerFactory.create(context)

    override suspend fun getAvailableVersionCode(): Int {
        return try {
            appUpdateManager.requestAppUpdateInfo()
                .availableVersionCode()
        } catch (e: Exception) {
            InAppUpdateManager.UNKNOWN_VERSION_CODE
        }
    }
}
