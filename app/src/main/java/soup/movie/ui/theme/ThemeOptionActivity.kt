package soup.movie.ui.theme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.BuildCompat
import kotlinx.android.synthetic.main.activity_theme_option.*
import soup.movie.R
import soup.movie.util.recreateWithAnimation

class ThemeOptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_option)
        listView.adapter = ThemeOptionListAdapter {
            //TODO: Save setting
            AppCompatDelegate.setDefaultNightMode(it.nightMode)
            recreateWithAnimation()
        }.apply {
            val defaultOption = if (BuildCompat.isAtLeastQ()) {
                ThemeOptionUiModel("시스템 기본값", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            } else {
                ThemeOptionUiModel("절전 모드 설정", AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
            }
            submitList(listOf(
                ThemeOptionUiModel("밝게", AppCompatDelegate.MODE_NIGHT_NO),
                ThemeOptionUiModel("어둡게", AppCompatDelegate.MODE_NIGHT_YES),
                defaultOption
            ))
        }
    }
}
