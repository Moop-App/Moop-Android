package soup.movie.ui.main.settings

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.transition.TransitionInflater
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_settings.*
import soup.movie.R
import soup.movie.ui.main.BaseTabFragment
import soup.movie.ui.theater.sort.TheaterSortActivity
import timber.log.Timber
import javax.inject.Inject

class SettingsFragment :
        BaseTabFragment<SettingsContract.View, SettingsContract.Presenter>(),
        SettingsContract.View {

    @Inject
    override lateinit var presenter: SettingsContract.Presenter

    override val layoutRes: Int
        get() = R.layout.fragment_settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = TransitionInflater.from(context)
                .inflateTransition(android.R.transition.explode)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTheaterButton.setOnClickListener { onTheaterEditClicked() }
    }

    override fun render(viewState: SettingsViewState) {
        Timber.d("render: %s", viewState)
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
