package soup.movie.ui.detail

import android.widget.TextView
import androidx.core.view.isEmpty
import androidx.databinding.BindingAdapter
import com.google.android.material.chip.ChipGroup
import soup.movie.R
import soup.movie.util.inflate

@BindingAdapter("genreList")
fun ChipGroup.setGenreList(genreList: List<String>) {
    if (isEmpty()) {
        genreList.forEach {
            val textView: TextView = inflate(context, R.layout.chip_genre)
            textView.text = it
            addView(textView)
        }
    }
}
