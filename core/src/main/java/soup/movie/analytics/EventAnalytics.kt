package soup.movie.analytics

import android.app.Activity

interface EventAnalytics {

    /* Common */
    fun screen(activity: Activity, screenName: String)

    /* Main */
    fun clickMovie()
    fun clickMenuFilter()

    /* Detail */
    fun clickPoster()
    fun clickShare()
    fun clickCgvInfo()
    fun clickLotteInfo()
    fun clickMegaboxInfo()

    /* Detail: Trailers */
    fun clickTrailer()
    fun clickMoreTrailers()
}
