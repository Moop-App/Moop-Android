package soup.movie.ui.main.settings

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.design.chip.Chip
import android.support.v4.app.SharedElementCallback
import android.transition.TransitionInflater
import android.util.Pair
import android.view.View
import butterknife.OnClick
import kotlinx.android.synthetic.main.fragment_settings.*
import soup.movie.R
import soup.movie.ui.main.BaseTabFragment
import soup.movie.ui.theater.sort.TheaterSortActivity
import timber.log.Timber
import javax.inject.Inject

class SettingsFragment
    : BaseTabFragment<SettingsContract.View, SettingsContract.Presenter>(),
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
            override fun onMapSharedElements(names: List<String>?,
                                             sharedElements: MutableMap<String, View>?) {
                for (name in names!!) {
                    val child = theater_group.findViewWithTag<View>(name)
                    sharedElements!![name] = child
                }
            }
        })
        postponeEnterTransition()
    }

    override fun render(viewState: SettingsViewState) {
        Timber.d("render: %s", viewState)
        val theaters = viewState.theaterList
        if (theaters.isEmpty()) {
            theater_empty.visibility = View.VISIBLE
            theater_group.removeAllViews()
            theater_group.visibility = View.GONE
        } else {
            theater_empty.visibility = View.GONE
            theater_group.removeAllViews()
            theater_group.visibility = View.VISIBLE

            for ((code, name) in theaters) {
                val theaterChip = View.inflate(context, R.layout.chip_cgv, null) as Chip
                theaterChip.chipText = name
                theaterChip.transitionName = code
                theaterChip.tag = code
                theater_group.addView(theaterChip)
            }
        }
    }

    @OnClick(R.id.theater_edit)
    fun onTheaterEditClicked() {
        val intent = Intent(context, TheaterSortActivity::class.java)
        startActivity(intent, ActivityOptions
                .makeSceneTransitionAnimation(activity, *createTheaterChipPairsForTransition())
                .toBundle())
    }

    private fun createTheaterChipPairsForTransition(): Array<Pair<View, String>> {
        theater_group.let {
            val childCount = it.childCount
            val pairs = arrayOf<Pair<View, String>>()
            for (i in 0 until childCount) {
                val v = it.getChildAt(i)
                pairs[i] = Pair.create(v, v.transitionName)
            }
            return pairs
        }
    }

    companion object {

        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}
