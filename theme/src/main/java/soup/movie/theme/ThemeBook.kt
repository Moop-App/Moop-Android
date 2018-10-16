package soup.movie.theme

import android.app.Activity
import android.app.Application

object ThemeBook {

    fun initialize(application: Application, default: ThemePage, vararg themes: ThemePage) {
        ThemeBookImpl.makeThemeBook(application, default, *themes)
    }

    fun open(activity: Activity) {
        ThemeBookImpl.open(activity)
    }

    fun turnOver(page: ThemePage, recreateAction: () -> Unit) {
        ThemeBookImpl.turnOver(page, recreateAction)
    }

    fun getAvailablePages(): List<ThemePage> {
        return ThemeBookImpl.getAvailablePages()
    }

    fun getBookmarkPage(): ThemePage {
        return ThemeBookImpl.getBookmarkPage()
    }
}
