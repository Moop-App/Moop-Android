package soup.movie.ui.main.settings

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_settings.*
import soup.movie.R
import soup.movie.databinding.FragmentSettingsBinding
import soup.movie.ui.main.BaseTabFragment
import soup.movie.ui.theater.sort.TheaterSortActivity
import soup.movie.util.log.printRenderLog
import javax.inject.Inject

class SettingsFragment :
        BaseTabFragment<SettingsContract.View, SettingsContract.Presenter>(),
        SettingsContract.View {

    @Inject
    override lateinit var presenter: SettingsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String>,
                                             sharedElements: MutableMap<String, View>) {
                names.forEach { name ->
                    theaterGroup.findViewWithTag<View>(name)?.let {
                        sharedElements[name] = it
                    }
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            FragmentSettingsBinding.inflate(inflater, container, false).root

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        editTheaterButton.setOnClickListener {
            onTheaterEditClicked()
        }
        paletteThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            presenter.setPaletteThemeSwitch(isChecked)
        }
    }

    override fun render(viewState: SettingsViewState) {
        viewState.printRenderLog()
        val theaters = viewState.theaterList
        if (theaters.isEmpty()) {
            noTheaterView.visibility = View.VISIBLE
            theaterGroup.removeAllViews()
            theaterGroup.visibility = View.GONE
        } else {
            noTheaterView.visibility = View.GONE
            theaterGroup.removeAllViews()
            theaterGroup.visibility = View.VISIBLE

            for ((code, name) in theaters) {
                val theaterChip = View.inflate(context, R.layout.chip_cgv, null) as Chip
                theaterChip.text = name
                theaterChip.transitionName = code
                theaterChip.tag = code
                theaterGroup.addView(theaterChip)
            }
        }
        paletteThemeSwitch.isChecked = viewState.usePaletteTheme
    }

    private fun onTheaterEditClicked() {
        val intent = Intent(context, TheaterSortActivity::class.java)
        startActivity(intent, ActivityOptions
                .makeSceneTransitionAnimation(activity, *createTheaterChipPairsForTransition())
                .toBundle())
    }

    private fun createTheaterChipPairsForTransition(): Array<Pair<View, String>> =
            when (theaterGroup) {
                null -> emptyArray()
                else -> mutableListOf<Pair<View, String>>().also { pair ->
                    theaterGroup.run {
                        repeat(childCount) {
                            getChildAt(it)?.run {
                                pair.add(Pair.create(this, transitionName))
                            }
                        }
                    }
                }.toTypedArray()
            }

    companion object {

        fun newInstance(): SettingsFragment = SettingsFragment()
    }
}
