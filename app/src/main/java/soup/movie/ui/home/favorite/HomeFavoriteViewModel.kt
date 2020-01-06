package soup.movie.ui.home.favorite

import androidx.lifecycle.ViewModel
import soup.movie.data.MoopRepository
import javax.inject.Inject

class HomeFavoriteViewModel @Inject constructor(
    private val repository: MoopRepository
) : ViewModel()
