package soup.movie.data.api

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import soup.movie.data.api.internal.ApiModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun moopApi(): MoopApiService

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApiComponent
    }

    companion object {
        fun factory(): Factory = DaggerApiComponent.factory()
    }
}
