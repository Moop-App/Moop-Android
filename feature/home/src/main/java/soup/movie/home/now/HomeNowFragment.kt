package soup.movie.home.now

import soup.movie.ext.assistedViewModels
import soup.movie.home.tab.HomeContentsFragment
import javax.inject.Inject

class HomeNowFragment : HomeContentsFragment() {

    @Inject
    lateinit var viewModelFactory: HomeNowViewModel.Factory
    override val viewModel: HomeNowViewModel by assistedViewModels {
        viewModelFactory.create()
    }
}
