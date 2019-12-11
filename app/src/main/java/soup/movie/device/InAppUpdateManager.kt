package soup.movie.device

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.ktx.requestAppUpdateInfo

interface InAppUpdateManager {

    suspend fun getAvailableVersionCode(): Int

    companion object {

        const val UNKNOWN_VERSION_CODE: Int = 0
    }
}

class ProductAppUpdateManager(context: Context) : InAppUpdateManager {

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
