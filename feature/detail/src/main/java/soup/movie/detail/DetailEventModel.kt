package soup.movie.detail

import android.net.Uri
import androidx.annotation.Keep
import androidx.annotation.StringRes

sealed class UiEvent

@Keep
class ShareAction(
    val target: ShareTarget,
    val imageUri: Uri,
    val mimeType: String
) : UiEvent()

enum class ShareTarget {
    Facebook,
    Twitter,
    Instagram,
    LINE,
    KakaoLink,
    Others
}

@Keep
class ToastAction(
    @StringRes
    val resId: Int
) : UiEvent()
