package soup.movie.ui.main.settings

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_settings.*
import soup.movie.BuildConfig
import soup.movie.R
import soup.movie.data.helper.Moob
import soup.movie.data.helper.getChipLayout
import soup.movie.databinding.FragmentSettingsBinding
import soup.movie.ui.helper.EventAnalytics
import soup.movie.ui.main.BaseTabFragment
import soup.movie.ui.main.settings.help.HelpFragment
import soup.movie.ui.theater.sort.TheaterSortActivity
import soup.movie.util.inflate
import soup.movie.util.log.printRenderLog
import soup.movie.util.setVisibleIf
import soup.movie.util.startActivitySafely
import javax.inject.Inject

class SettingsFragment :
        BaseTabFragment<SettingsContract.View, SettingsContract.Presenter>(),
        SettingsContract.View {

    @Inject
    override lateinit var presenter: SettingsContract.Presenter

    @Inject
    lateinit var analytics: EventAnalytics

    override val menuResource: Int?
        get() = R.menu.fragment_settings

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_help) {
            if (panelIsShown()) {
                hidePanel()
            } else {
                analytics.clickMenuHelp()
                showPanel(HelpFragment.toPanelData())
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

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
        usePaletteThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            presenter.setUsePaletteTheme(isChecked)
        }
        useWebLinkSwitch.setOnCheckedChangeListener { _, isChecked ->
            presenter.setUseWebLink(isChecked)
        }
        bugReportButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, BuildConfig.HELP_E_MAIL)
            intent.putExtra(Intent.EXTRA_SUBJECT, "뭅 v${BuildConfig.VERSION_NAME} 버그리포트")
            intent.putExtra(Intent.EXTRA_TEXT, "") //TODO
            it.context.startActivitySafely(intent)
        }
        appVersionLabel.text = "현재 ${BuildConfig.VERSION_NAME}"
        appVersionButton.setOnClickListener {
            Moob.executePlayStore(requireContext())
        }
    }

    override fun render(viewState: SettingsViewState) {
        printRenderLog { viewState }
        val theaters = viewState.theaterList
        noTheaterView?.setVisibleIf { theaters.isEmpty() }
        theaterGroup?.setVisibleIf { theaters.isNotEmpty() }
        theaterGroup?.run {
            removeAllViews()
            theaters.map {
                inflate<Chip>(context, it.getChipLayout()).apply {
                    text = it.name
                    transitionName = it.code
                    tag = it.code
                }
            }.forEach { addView(it) }
        }
        usePaletteThemeSwitch?.isChecked = viewState.usePaletteTheme
        useWebLinkSwitch?.isChecked = viewState.useWebLink
        appVersionLabel?.text = "현재 ${BuildConfig.VERSION_NAME} / 최신 ${viewState.version.code}"
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
