package soup.movie

import android.content.Context

class CredentialsImpl(private val appContext: Context) : Credentials {

    override fun getKakaoAppKey(): String {
        return appContext.getString(R.string.kakao_app_key)
    }
}
