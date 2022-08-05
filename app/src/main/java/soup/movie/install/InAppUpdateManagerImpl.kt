/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soup.movie.install

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.suspendCoroutine

class InAppUpdateManagerImpl(
    context: Context,
    private val ioDispatcher: CoroutineDispatcher,
) : InAppUpdateManager {

    private val appUpdateManager = AppUpdateManagerFactory.create(context)

    override suspend fun getAvailableVersionCode(): Int {
        return withContext(ioDispatcher) {
            try {
                appUpdateManager.requestAppUpdateInfo()
                    .availableVersionCode()
            } catch (e: Exception) {
                Timber.w(e)
                InAppUpdateManager.UNKNOWN_VERSION_CODE
            }
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
