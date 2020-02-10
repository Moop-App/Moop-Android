package soup.movie.detail.di

import androidx.fragment.app.FragmentActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import soup.movie.detail.DetailActivity

@Module
abstract class DetailActivityModule {

    @Binds
    abstract fun providesActivity(detailActivity: DetailActivity): FragmentActivity

    @Module
    abstract class DetailActivityBuilder {

        @ContributesAndroidInjector(
            modules = [
                DetailActivityModule::class,
                DetailAssistedInjectModule::class
            ]
        )
        abstract fun contributeMainActivity(): DetailActivity
    }
}
