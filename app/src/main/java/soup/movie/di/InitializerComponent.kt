package soup.movie.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import soup.movie.init.WorkManagerInitializer

@Component(dependencies = [InitializerDependencies::class])
interface InitializerComponent {

    fun inject(initializer: WorkManagerInitializer)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(dependencies: InitializerDependencies): Builder
        fun build(): InitializerComponent
    }
}
