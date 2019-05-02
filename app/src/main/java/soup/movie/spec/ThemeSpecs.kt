package soup.movie.spec

import android.app.Activity
import soup.movie.R
import soup.movie.theme.ThemePage
import soup.movie.ui.detail.DetailActivity
import soup.movie.ui.main.MainActivity
import soup.movie.ui.search.SearchActivity
import soup.movie.ui.theater.edit.TheaterEditActivity
import soup.movie.ui.theater.sort.TheaterSortActivity

object ThemeSpecs {

    /**
     * WARNING: ID 값은 임의로 수정하지 마세요.
     */
    private const val ID_DEFAULT = "THEME_DEFAULT"
    private const val ID_BLACK = "THEME_BLACK"

    internal val DEFAULT = object : ThemePage(ID_DEFAULT, R.string.theme_default) {

        override fun extractThemeOf(activity: Activity?): Int = when (activity) {
            is MainActivity -> R.style.AppTheme
            is SearchActivity -> R.style.AppTheme_Search
            is DetailActivity -> R.style.AppTheme_Detail
            is TheaterSortActivity -> R.style.AppTheme_TheaterSort
            is TheaterEditActivity -> R.style.AppTheme_TheaterEdit
            else -> R.style.AppTheme
        }
    }

    internal val BLACK = object : ThemePage(ID_BLACK, R.string.theme_black) {

        override fun extractThemeOf(activity: Activity?): Int = when (activity) {
            is MainActivity -> R.style.BlackTheme
            is SearchActivity -> R.style.BlackTheme_Search
            is DetailActivity -> R.style.BlackTheme_Detail
            is TheaterSortActivity -> R.style.BlackTheme_TheaterSort
            is TheaterEditActivity -> R.style.BlackTheme_TheaterEdit
            else -> R.style.BlackTheme
        }
    }
}
