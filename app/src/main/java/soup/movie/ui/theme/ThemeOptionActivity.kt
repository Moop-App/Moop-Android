package soup.movie.ui.theme

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_theme_option.*
import soup.movie.R
import soup.movie.ui.BaseActivity
import soup.movie.util.observe
import soup.movie.util.recreateWithAnimation

class ThemeOptionActivity : BaseActivity() {

    private val viewModel: ThemeOptionViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_option)
        val listAdapter = ThemeOptionListAdapter {
            viewModel.onItemClick(it)
            recreateWithAnimation()
        }
        listView.adapter = listAdapter
        viewModel.uiModel.observe(this) {
            listAdapter.submitList(it.items)
        }
    }
}
