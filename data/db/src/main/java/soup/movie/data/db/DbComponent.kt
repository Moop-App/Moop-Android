package soup.movie.data.db

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import soup.movie.data.db.internal.DbModule
import javax.inject.Singleton

@Singleton
@Component(modules = [DbModule::class])
interface DbComponent {

    fun moopDatabase(): MoopDatabase

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): DbComponent
    }

    companion object {
        fun factory(): Factory = DaggerDbComponent.factory()
    }
}
