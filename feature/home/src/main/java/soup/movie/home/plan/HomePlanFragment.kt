package soup.movie.home.plan

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import soup.movie.home.tab.HomeContentsFragment

@AndroidEntryPoint
class HomePlanFragment : HomeContentsFragment() {

    override val viewModel: HomePlanViewModel by viewModels()
}
