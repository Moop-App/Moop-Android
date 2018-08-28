package soup.movie.ui.main.settings

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.design.chip.Chip
import android.support.v4.app.SharedElementCallback
import android.util.Pair
import android.view.View
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
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
//        exitTransition = TransitionInflater.from(context)
//                .inflateTransition(android.R.transition.explode)
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String>,
                                             sharedElements: MutableMap<String, View>) {
                names.forEach { name ->
                    theater_group.findViewWithTag<View>(name)?.let {
                        sharedElements[name] = it
                    }
                }
            }
        })
        postponeEnterTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.theater_edit.setOnClickListener { onTheaterEditClicked() }
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

    private fun onTheaterEditClicked() {
        val intent = Intent(context, TheaterSortActivity::class.java)
        startActivity(intent, ActivityOptions
                .makeSceneTransitionAnimation(activity, *createTheaterChipPairsForTransition())
                .toBundle())
    }

    private fun createTheaterChipPairsForTransition(): Array<Pair<View, String>> {
        view?.theater_group?.let {
            val pairs = mutableListOf<Pair<View, String>>()
            (0 until it.childCount).forEach { i ->
                val v = it.getChildAt(i)
                pairs.add(Pair.create(v, v.transitionName))
            }
            return pairs.toTypedArray()
        } ?: return emptyArray()
    }

    companion object {

        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}
