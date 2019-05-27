package soup.movie.theme

interface ThemeOptionStore {

    fun save(option: ThemeOption)

    fun restore(): ThemeOption
}
