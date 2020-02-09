package soup.movie.theme

interface ThemeOptionStore {

    fun save(option: String)

    fun restore(): String
}
