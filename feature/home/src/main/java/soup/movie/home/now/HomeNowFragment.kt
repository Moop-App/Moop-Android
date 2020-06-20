package soup.movie.home.now

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import soup.movie.home.tab.HomeContentsFragment

@AndroidEntryPoint
class HomeNowFragment : HomeContentsFragment() {

    override val viewModel: HomeNowViewModel by viewModels()
}
