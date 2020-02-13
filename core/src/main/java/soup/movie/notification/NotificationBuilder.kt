package soup.movie.notification

import soup.movie.model.Movie
import soup.movie.model.OpenDateAlarm

interface NotificationBuilder {

    fun showLegacyNotification(list: List<Movie>)
    fun showAlarmNotification(list: List<OpenDateAlarm>)
}
