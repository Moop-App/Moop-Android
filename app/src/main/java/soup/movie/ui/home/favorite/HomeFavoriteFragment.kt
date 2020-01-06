package soup.movie.ui.home.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import soup.movie.databinding.HomeContentsBinding
import soup.movie.ui.base.BaseFragment

class HomeFavoriteFragment : BaseFragment() {

    private lateinit var binding: HomeContentsBinding
    private val viewModel: HomeFavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}
