package soup.movie.settings.model

class AgeFilter(private val flags: Int) {

    fun hasAll(): Boolean = has(FLAG_AGE_ALL)
    fun has12(): Boolean = has(FLAG_AGE_12)
    fun has15(): Boolean = has(FLAG_AGE_15)
    fun has19(): Boolean = has(FLAG_AGE_19)

    private fun has(flag: Int): Boolean {
        return (flags and flag) != 0
    }

    fun toFlags(): Int = flags

    companion object {

        const val FLAG_AGE_ALL: Int = 0x1
        const val FLAG_AGE_12: Int = 0x2
        const val FLAG_AGE_15: Int = 0x4
        const val FLAG_AGE_19: Int = 0x8
        const val FLAG_AGE_DEFAULT: Int = FLAG_AGE_ALL or FLAG_AGE_12 or FLAG_AGE_15 or FLAG_AGE_19
    }
}
