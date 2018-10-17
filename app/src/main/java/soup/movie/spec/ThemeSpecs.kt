package soup.movie.spec

import android.app.Activity
import soup.movie.R
import soup.movie.theme.ThemePage
import soup.movie.ui.detail.DetailActivity
import soup.movie.ui.detail.timetable.TimetableActivity
import soup.movie.ui.main.MainActivity
import soup.movie.ui.theater.edit.TheaterEditActivity
import soup.movie.ui.theater.sort.TheaterSortActivity

object ThemeSpecs {

    /**
     * WARNING: ID 값은 임의로 수정하지 마세요.
     */
    private const val ID_DEFAULT = "THEME_DEFAULT"
    private const val ID_BLACK = "THEME_BLACK"
    private const val ID_WA_TGBH = "THEME_WA_TGBH"

    internal val DEFAULT = object : ThemePage(ID_DEFAULT, "Default") {

        override fun extractThemeOf(activity: Activity?): Int = when (activity) {
            is MainActivity -> R.style.AppTheme
            is DetailActivity -> R.style.AppTheme_Detail
            is TimetableActivity -> R.style.AppTheme_Timetable
            is TheaterSortActivity -> R.style.AppTheme_TheaterSort
            is TheaterEditActivity -> R.style.AppTheme_TheaterEdit
            else -> R.style.AppTheme
        }
    }

    internal val BLACK = object : ThemePage(ID_BLACK, "Black") {

        override fun extractThemeOf(activity: Activity?): Int = when (activity) {
            is MainActivity -> R.style.BlackTheme
            is DetailActivity -> R.style.BlackTheme_Detail
            is TimetableActivity -> R.style.BlackTheme_Timetable
            is TheaterSortActivity -> R.style.BlackTheme_TheaterSort
            is TheaterEditActivity -> R.style.BlackTheme_TheaterEdit
            else -> R.style.BlackTheme
        }
    }

    internal val WA_TGBH = object : ThemePage(ID_WA_TGBH, "T.G.B.H.") {

        override fun extractThemeOf(activity: Activity?): Int = when (activity) {
            is MainActivity -> R.style.WaTgbhTheme
            is DetailActivity -> R.style.WaTgbhTheme_Detail
            is TimetableActivity -> R.style.WaTgbhTheme_Timetable
            is TheaterSortActivity -> R.style.WaTgbhTheme_TheaterSort
            is TheaterEditActivity -> R.style.WaTgbhTheme_TheaterEdit
            else -> R.style.WaTgbhTheme
        }
    }
}
