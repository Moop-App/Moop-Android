package soup.movie.install

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import kotlin.coroutines.suspendCoroutine

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

    private suspend fun AppUpdateManager.requestAppUpdateInfo(): AppUpdateInfo {
        return suspendCoroutine { continuation ->
            appUpdateInfo
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(it))
                }
                .addOnFailureListener {
                    continuation.resumeWith(Result.failure(it))
                }
        }
    }
}
