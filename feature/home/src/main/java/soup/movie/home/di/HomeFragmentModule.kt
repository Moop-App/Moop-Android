package soup.movie.home.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import soup.movie.home.favorite.HomeFavoriteFragment
import soup.movie.home.now.HomeNowFragment
import soup.movie.home.plan.HomePlanFragment

@Module
interface HomeFragmentModule {

    @ContributesAndroidInjector
    fun bindHomeNowFragment(): HomeNowFragment

    @ContributesAndroidInjector
    fun bindHomePlanFragment(): HomePlanFragment

    @ContributesAndroidInjector
    fun bindHomeFavoriteFragment(): HomeFavoriteFragment
}
