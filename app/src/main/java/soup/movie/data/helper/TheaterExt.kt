package soup.movie.data.helper

import androidx.annotation.LayoutRes
import com.mapbox.mapboxsdk.geometry.LatLng
import soup.movie.R
import soup.movie.data.model.Theater
import soup.movie.data.model.Theater.Companion.TYPE_CGV
import soup.movie.data.model.Theater.Companion.TYPE_LOTTE
import soup.movie.data.model.Theater.Companion.TYPE_MEGABOX

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
fun Theater.getSortChipLayout(): Int {
    return when(type) {
        TYPE_CGV -> R.layout.item_theater_cgv
        TYPE_LOTTE -> R.layout.item_theater_lotte
        TYPE_MEGABOX -> R.layout.item_theater_megabox
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

fun Theater.fullName(): String = when (type) {
    TYPE_CGV -> "CGV $name"
    TYPE_LOTTE -> "롯데시네마 $name"
    TYPE_MEGABOX -> "메가박스 $name"
    else -> throw IllegalArgumentException("$type is not valid type.")
}

fun Theater.position(): LatLng = LatLng(lat, lng)
