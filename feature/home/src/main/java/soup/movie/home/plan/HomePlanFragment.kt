package soup.movie.home.plan

import soup.movie.ext.assistedViewModels
import soup.movie.home.tab.HomeContentsFragment
import javax.inject.Inject

class HomePlanFragment : HomeContentsFragment() {

    @Inject
    lateinit var viewModelFactory: HomePlanViewModel.Factory
    override val viewModel: HomePlanViewModel by assistedViewModels {
        viewModelFactory.create()
    }
}
