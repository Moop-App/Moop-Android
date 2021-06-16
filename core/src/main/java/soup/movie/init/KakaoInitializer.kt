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
package soup.movie.init

import android.content.Context
import androidx.startup.Initializer
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.EntryPointAccessors
import soup.movie.Credentials
import soup.movie.core.di.DaggerInitializerComponent
import soup.movie.core.di.InitializerDependencies
import javax.inject.Inject

class KakaoInitializer : Initializer<Unit> {

    @Inject
    lateinit var credentials: Credentials

    override fun create(context: Context) {
        DaggerInitializerComponent.builder()
            .context(context)
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    context.applicationContext,
                    InitializerDependencies::class.java
                )
            )
            .build()
            .inject(this)

        KakaoSdk.init(context, credentials.getKakaoAppKey())
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
