package soup.movie.detail

import android.net.Uri
import androidx.annotation.Keep

@Keep
class ShareAction(
    val target: ShareTarget,
    val imageUri: Uri,
    val mimeType: String
)

enum class ShareTarget {
    Facebook,
    Twitter,
    Instagram,
    LINE,
    KakaoLink,
    Others
}
