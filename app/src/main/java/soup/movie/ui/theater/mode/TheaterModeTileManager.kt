package soup.movie.ui.theater.mode

typealias Listener = (TheaterModeTileManager.TileState) -> Unit

object TheaterModeTileManager {

    enum class TileState {
        Active, Inactive
    }

    private var listener: Listener? = null

    var tileState: TileState = TileState.Inactive
        set(value) {
            if (field != value) {
                field = value
                listener?.invoke(value)
            }
        }

    fun setListener(listener: Listener?) {
        this.listener = listener
        listener?.invoke(tileState)
    }
}
