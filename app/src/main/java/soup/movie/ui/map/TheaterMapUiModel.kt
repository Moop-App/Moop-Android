package soup.movie.ui.map

class TheaterMapUiModel(
    val theaterMarkerList: List<TheaterMarkerUiModel>
)

sealed class TheaterMarkerUiModel {
    abstract val areaCode: String
    abstract val code: String
    abstract val name: String
    abstract val lng: Double
    abstract val lat: Double
}

class CgvMarkerUiModel(
    override val areaCode: String,
    override val code: String,
    override val name: String,
    override val lng: Double,
    override val lat: Double
) : TheaterMarkerUiModel()

class LotteCinemaMarkerUiModel(
    override val areaCode: String,
    override val code: String,
    override val name: String,
    override val lng: Double,
    override val lat: Double
) : TheaterMarkerUiModel()

class MegaboxMarkerUiModel(
    override val areaCode: String,
    override val code: String,
    override val name: String,
    override val lng: Double,
    override val lat: Double
) : TheaterMarkerUiModel()
