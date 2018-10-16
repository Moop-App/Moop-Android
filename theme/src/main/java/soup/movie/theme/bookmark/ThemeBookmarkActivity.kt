package soup.movie.theme.bookmark

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_theme_bookmark.*
import soup.movie.theme.R
import soup.movie.theme.ThemeBook

class ThemeBookmarkActivity : AppCompatActivity() {

    private val listAdapter = ThemePageListAdapter {
        ThemeBook.turnOver(this, it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeBook.open(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_bookmark)

        listView.adapter = listAdapter
        listAdapter.submitList(ThemeBook.getAvailablePages())
    }
}
