package soup.movie.util.log

object LogConstants {

    const val RENDER_MESSAGE_PREFIX: String = "render: "
    const val RENDER_MESSAGE: String = "$RENDER_MESSAGE_PREFIX%s"
}

fun Any?.printRenderLog() {
    timber.log.Timber.d(LogConstants.RENDER_MESSAGE, toString())
}
