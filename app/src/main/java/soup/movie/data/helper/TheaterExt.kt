package soup.movie.data.helper

import android.content.Context
import androidx.annotation.LayoutRes
import soup.movie.R
import soup.movie.data.model.Theater
import soup.movie.data.model.Theater.Companion.TYPE_CGV
import soup.movie.data.model.Theater.Companion.TYPE_LOTTE
import soup.movie.data.model.Theater.Companion.TYPE_MEGABOX
import soup.movie.util.helper.Cgv
import soup.movie.util.helper.LotteCinema
import soup.movie.util.helper.Megabox

@LayoutRes
fun Theater.getChipLayout(): Int {
    return when(type) {
        TYPE_CGV -> R.layout.chip_action_cgv
        TYPE_LOTTE -> R.layout.chip_action_lotte
        TYPE_MEGABOX -> R.layout.chip_action_megabox
        else -> throw IllegalArgumentException("$type is not valid type.")
    }
}

@LayoutRes
fun Theater.getFilterChipLayout(): Int {
    return when(type) {
        TYPE_CGV -> R.layout.chip_filter_cgv
        TYPE_LOTTE -> R.layout.chip_filter_lotte
        TYPE_MEGABOX -> R.layout.chip_filter_megabox
        else -> throw IllegalArgumentException("$type is not valid type.")
    }
}

fun Theater.executeWeb(ctx: Context) {
    return when (type) {
        TYPE_CGV -> Cgv.executeWeb(ctx, this)
        TYPE_LOTTE -> LotteCinema.executeWeb(ctx, this)
        TYPE_MEGABOX -> Megabox.executeWeb(ctx, this)
        else -> throw IllegalArgumentException("$type is not valid type.")
    }
}
