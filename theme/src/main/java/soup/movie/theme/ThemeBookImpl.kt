package soup.movie.theme

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.collection.ArrayMap
import kotlin.reflect.KClass

internal object ThemeBookImpl {

    private val pagePerActivity = ArrayMap<KClass<out Activity>, String>()

    private lateinit var defaultPage: ThemePage
    private lateinit var availablePages: List<ThemePage>
    private lateinit var currentPagePref: CurrentPagePref

    internal fun makeThemeBook(application: Application, default: ThemePage, vararg themes: ThemePage) {
        if(::availablePages.isInitialized) {
            throw ThemeBookAlreadyInitializedException("Theme book can be made a single copy only.")
        }
        defaultPage = default
        availablePages = listOf(default, *themes)
        currentPagePref = CurrentPagePref(application, default)
        application.registerActivityLifecycleCallbacks(LifecycleListener())
    }

    internal fun open(activity: Activity) {
        val page = getBookmarkPage()
        activity.setTheme(page.extractThemeOf(activity))
        pagePerActivity[activity::class] = page.id
    }

    internal fun getBookmarkPage(): ThemePage {
        val pageId = currentPagePref.get()
        return availablePages.find { it.id == pageId } ?: defaultPage
    }

    internal fun getAvailablePages(): List<ThemePage> {
        return availablePages
    }

    internal fun turnOver(activity: Activity, page: ThemePage) {
        if (availablePages.all { it.id != page.id }) {
            throw ThemePagesNotRegisteredException("Theme page '$page.id' does not exists.")
        }
        if (currentPagePref.set(page)) {
            activity.recreate()
        }
    }

    class ThemePagesNotRegisteredException(message: String) : IllegalAccessException(message)
    class ThemeBookAlreadyInitializedException(message: String) : IllegalAccessException(message)

    internal class LifecycleListener : Application.ActivityLifecycleCallbacks {

        override fun onActivityResumed(activity: Activity) {
            val pageId = pagePerActivity[activity::class] ?: return
            if (currentPagePref.get() != pageId) {
                activity.recreate()
            }
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
        override fun onActivityStarted(activity: Activity) {}
        override fun onActivityPaused(activity: Activity) {}
        override fun onActivityStopped(activity: Activity) {}
        override fun onActivityDestroyed(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}
    }

    private class CurrentPagePref(ctx: Context, default: ThemePage) {

        private val themePreferences: SharedPreferences = ctx.applicationContext
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        private var currentPageId: String

        init {
            currentPageId = themePreferences.getString(PREFS_KEY, null) ?: default.id
        }

        fun get(): String = currentPageId

        fun set(page: ThemePage): Boolean {
            val pageId = page.id
            if (currentPageId != pageId) {
                currentPageId = pageId
                themePreferences.edit().putString(PREFS_KEY, pageId).apply()
                return true
            }
            return false
        }

        companion object {

            private const val PREFS_NAME = "soup.movie.theme.prefs"
            private const val PREFS_KEY = "soup.movie.theme.current"
        }
    }
}
