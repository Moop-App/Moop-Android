package soup.movie.device

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface InAppUpdateManager {

    suspend fun getAvailableVersionCode(): Int
}

class ProductAppUpdateManager(context: Context) : InAppUpdateManager {

    private val appUpdateManager = AppUpdateManagerFactory.create(context)

    override suspend fun getAvailableVersionCode(): Int =
        suspendCoroutine { continuation ->
            appUpdateManager.appUpdateInfo
                .addOnSuccessListener { continuation.resume(it.availableVersionCode()) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
}
