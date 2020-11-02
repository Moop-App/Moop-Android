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
