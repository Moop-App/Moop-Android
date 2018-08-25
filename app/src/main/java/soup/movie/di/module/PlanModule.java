package soup.movie.di.module;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import soup.movie.di.scope.FragmentScoped;
import soup.movie.ui.main.plan.PlanContract;
import soup.movie.ui.main.plan.PlanFragment;
import soup.movie.ui.main.plan.PlanPresenter;
import soup.movie.ui.main.plan.VerticalPlanFragment;

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
