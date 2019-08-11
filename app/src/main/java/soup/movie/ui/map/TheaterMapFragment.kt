package soup.movie.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import soup.movie.databinding.TheaterMapFragmentBinding
import soup.movie.ui.base.BaseFragment
import soup.movie.ui.main.MainViewModel
import soup.movie.util.doOnApplyWindowInsets

class TheaterMapFragment : BaseFragment() {

    private lateinit var binding: TheaterMapFragmentBinding

    private val activityViewModel: MainViewModel by activityViewModels()
    private val viewModel: TheaterMapViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TheaterMapFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.initViewState()
        binding.adaptSystemWindowInset()
        return binding.root
    }

    private fun TheaterMapFragmentBinding.initViewState() {
        header.apply {
            toolbar.setNavigationOnClickListener {
                activityViewModel.openNavigationMenu()
            }
        }
    }

    private fun TheaterMapFragmentBinding.adaptSystemWindowInset() {
        theaterMapScene.doOnApplyWindowInsets { view, windowInsets, initialPadding ->
            view.updatePadding(
                top = initialPadding.top + windowInsets.systemWindowInsetTop
            )
        }
    }
}
