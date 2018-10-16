package soup.movie.util

import android.app.Activity

fun Activity.recreateWithAnimation(enterAnim: Int = android.R.anim.fade_in,
                                   exitAnim: Int = android.R.anim.fade_out) {
    finish()
    overridePendingTransition(enterAnim, exitAnim)
    startActivity(intent)
}
