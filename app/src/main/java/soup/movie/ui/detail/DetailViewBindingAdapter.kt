package soup.movie.ui.detail

import androidx.core.view.isEmpty
import androidx.databinding.BindingAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import soup.movie.R
import soup.movie.util.inflate

@BindingAdapter("genreList")
fun ChipGroup.setGenreList(genreList: List<String>) {
    if (isEmpty()) {
        genreList.forEach {
            val chip: Chip = inflate(context, R.layout.chip_action_genre)
            chip.text = it
            addView(chip)
        }
    }
}
