package soup.movie.ui.settings

import android.os.Bundle
import soup.movie.R
import soup.movie.ui.BaseActivity

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, SettingsFragment())
                .commit()
        }
    }
}
