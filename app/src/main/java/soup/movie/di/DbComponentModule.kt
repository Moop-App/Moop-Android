package soup.movie.di

import android.app.Application
import dagger.Module
import dagger.Provides
import soup.movie.data.db.DbComponent
import soup.movie.data.db.MoopDatabase
import javax.inject.Singleton

@Module
object DbComponentModule {

    @Singleton
    @Provides
    fun provideMoopDatabase(
        application: Application
    ): MoopDatabase {
        return DbComponent.factory()
            .create(application)
            .moopDatabase()
    }
}
