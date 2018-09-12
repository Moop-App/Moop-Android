package soup.movie.util

import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import com.kakao.util.helper.Utility
import timber.log.Timber
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object DeviceUtil {

    fun getKeyHash(context: Context): String? {
        Utility.getPackageInfo(context, PackageManager.GET_SIGNATURES)?.signatures?.forEach {
            try {
                return MessageDigest.getInstance ("SHA")
                        ?.apply { update(it.toByteArray()) }
                        ?.run { digest().encodeToBase64() }
            } catch (e: NoSuchAlgorithmException) {
                Timber.w(e, "Unable to get MessageDigest. signature=%s", it)
            }
        }
        return null
    }

    private fun ByteArray?.encodeToBase64(): String? {
        return Base64.encodeToString(this, Base64.NO_WRAP)
    }
}