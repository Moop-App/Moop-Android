package soup.movie.data.helper

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import com.mapbox.mapboxsdk.geometry.LatLng
import soup.movie.R
import soup.movie.data.model.Theater
import soup.movie.data.model.Theater.Companion.TYPE_CGV
import soup.movie.data.model.Theater.Companion.TYPE_LOTTE
import soup.movie.data.model.Theater.Companion.TYPE_MEGABOX

@LayoutRes
fun Theater.getTimetableLayout(): Int {
    return when(type) {
        TYPE_CGV -> R.layout.item_timetable_theater_cgv
        TYPE_LOTTE -> R.layout.item_timetable_theater_lotte
        TYPE_MEGABOX -> R.layout.item_timetable_theater_megabox
        else -> throw IllegalArgumentException("$type is not valid type.")
    }
}

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
        TYPE_CGV -> R.layout.item_sort_theater_cgv
        TYPE_LOTTE -> R.layout.item_sort_theater_lotte
        TYPE_MEGABOX -> R.layout.item_sort_theater_megabox
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

@DrawableRes
fun Theater.getMarkerIcon(): Int {
    return when(type) {
        TYPE_CGV -> R.drawable.ic_marker_cgv
        TYPE_LOTTE -> R.drawable.ic_marker_lotte
        TYPE_MEGABOX -> R.drawable.ic_marker_megabox
        else -> throw IllegalArgumentException("$type is not valid type.")
    }
}

@DrawableRes
fun Theater.getSelectedMarkerIcon(): Int {
    return when(type) {
        TYPE_CGV -> R.drawable.ic_marker_cgv_selected
        TYPE_LOTTE -> R.drawable.ic_marker_lotte_selected
        TYPE_MEGABOX -> R.drawable.ic_marker_megabox_selected
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

fun Theater.toMapIntent(): Intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("geo:0,0?q=$lat,$lng(${fullName()})"))

fun Theater.executeWeb(ctx: Context) {
    return when (type) {
        TYPE_CGV -> Cgv.executeWeb(ctx, this)
        TYPE_LOTTE -> LotteCinema.executeWeb(ctx, this)
        TYPE_MEGABOX -> Megabox.executeWeb(ctx, this)
        else -> throw IllegalArgumentException("$type is not valid type.")
    }
}
