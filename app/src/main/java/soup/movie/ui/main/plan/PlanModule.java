package soup.movie.ui.main.plan;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import soup.movie.di.FragmentScoped;

@Module
public abstract class PlanModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract PlanFragment planFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract VerticalPlanFragment verticalPlanFragment();

    @FragmentScoped
    @Binds
    abstract PlanContract.Presenter planPresenter(PlanPresenter presenter);
}
