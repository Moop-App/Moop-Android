package soup.movie.theme

object ThemeBook {

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
