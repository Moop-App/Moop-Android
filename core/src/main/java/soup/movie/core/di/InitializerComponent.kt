package soup.movie.core.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import soup.movie.init.KakaoInitializer

@Component(dependencies = [InitializerDependencies::class])
interface InitializerComponent {

    fun inject(initializer: KakaoInitializer)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(dependencies: InitializerDependencies): Builder
        fun build(): InitializerComponent
    }
}
