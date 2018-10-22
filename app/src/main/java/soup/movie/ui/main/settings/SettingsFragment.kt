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
import androidx.appcompat.app.AlertDialog
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.item_settings_experimental.*
import kotlinx.android.synthetic.main.item_settings_feedback.*
import kotlinx.android.synthetic.main.item_settings_theater.*
import kotlinx.android.synthetic.main.item_settings_theme.*
import kotlinx.android.synthetic.main.item_settings_version.*
import soup.movie.BuildConfig
import soup.movie.R
import soup.movie.data.helper.Moop
import soup.movie.data.helper.executeWeb
import soup.movie.data.helper.getChipLayout
import soup.movie.databinding.FragmentSettingsBinding
import soup.movie.theme.ThemeBook
import soup.movie.ui.helper.EventAnalytics
import soup.movie.ui.main.BaseTabFragment
import soup.movie.ui.main.settings.help.HelpFragment
import soup.movie.ui.theater.sort.TheaterSortActivity
import soup.movie.ui.theme.ThemeBookmarkActivity
import soup.movie.util.*
import soup.movie.util.log.printRenderLog
import javax.inject.Inject

class SettingsFragment :
        BaseTabFragment<SettingsContract.View, SettingsContract.Presenter>(),
        SettingsContract.View {

    @Inject
    override lateinit var presenter: SettingsContract.Presenter

    @Inject
    lateinit var analytics: EventAnalytics

    private var versionViewState: SettingsViewState.VersionViewState? = null

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

    override fun onMapSharedElements(names: List<String>,
                                     sharedElements: MutableMap<String, View>) {
        sharedElements.clear()
        names.forEach { name ->
            theaterGroup.findViewWithTag<View>(name)?.let {
                sharedElements[name] = it
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            FragmentSettingsBinding.inflate(inflater, container, false)
                    .apply { item = ThemeBook.getBookmarkPage() }
                    .root

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        editTheaterButton.setOnDebounceClickListener {
            onTheaterEditClicked()
        }
        editThemeButton.setOnDebounceClickListener {
            onThemeEditClicked()
        }
        themeGroup.setOnDebounceClickListener {
            onThemeEditClicked()
        }
        usePaletteThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            presenter.setUsePaletteTheme(isChecked)
        }
        useWebLinkSwitch.setOnCheckedChangeListener { _, isChecked ->
            presenter.setUseWebLink(isChecked)
        }
        bugReportButton.setOnDebounceClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(BuildConfig.HELP_E_MAIL))
            intent.putExtra(Intent.EXTRA_SUBJECT, "뭅 v${BuildConfig.VERSION_NAME} 버그리포트")
            intent.putExtra(Intent.EXTRA_TEXT, "") //TODO
            it.context.startActivitySafely(intent)
        }
        versionButton.setOnDebounceClickListener {
            onVersionClicked()
        }
    }

    override fun render(viewState: SettingsViewState.TheaterListViewState) {
        printRenderLog { viewState }
        val theaters = viewState.theaterList
        noTheaterView?.setVisibleIf { theaters.isEmpty() }
        theaterGroup?.setVisibleIf { theaters.isNotEmpty() }
        theaterGroup?.run {
            removeAllViews()
            theaters.map {
                inflate<Chip>(context, it.getChipLayout()).apply {
                    text = it.name
                    transitionName = it.id
                    tag = it.id
                    setOnDebounceClickListener { _ -> it.executeWeb(requireContext()) }
                }
            }.forEach { addView(it) }
        }
    }

    override fun render(viewState: SettingsViewState.ExperimentalViewState) {
        printRenderLog { viewState }
        usePaletteThemeSwitch?.isChecked = viewState.usePaletteTheme
        useWebLinkSwitch?.isChecked = viewState.useWebLink
    }

    override fun render(viewState: SettingsViewState.VersionViewState) {
        printRenderLog { viewState }
        versionViewState = viewState
        currentVersionLabel?.text = getString(R.string.settings_version_current, viewState.current.versionName)
        latestVersionLabel?.text = getString(R.string.settings_version_latest, viewState.latest.versionName)
        newReleaseIcon?.setGoneIf { viewState.isLatest() }
        if (viewState.isLatest().not()) {
            AlertDialog.Builder(requireContext())
                    .setIcon(R.drawable.ic_round_new_releases)
                    .setTitle(R.string.settings_version_update_title)
                    .setMessage(getString(R.string.settings_version_update_message, viewState.latest.versionName))
                    .setPositiveButton(R.string.settings_version_update_button_positive) {
                        _, _ -> Moop.executePlayStore(requireContext())
                    }
                    .setNegativeButton(R.string.settings_version_update_button_negative) {
                        dialog, _ -> dialog.dismiss()
                    }
                    .show()
        }
    }

    private fun onTheaterEditClicked() {
        val intent = Intent(context, TheaterSortActivity::class.java)
        startActivityForResult(intent, 0, ActivityOptions
                .makeSceneTransitionAnimation(activity, *createSharedElementsForTheaters())
                .toBundle())
    }

    private fun createSharedElementsForTheaters(): Array<Pair<View, String>> =
            theaterGroup?.run {
                (0 until childCount)
                        .mapNotNull { getChildAt(it) }
                        .map { it with it.transitionName }
                        .toTypedArray()
            } ?: emptyArray()

    private fun onThemeEditClicked() {
        val intent = Intent(requireActivity(), ThemeBookmarkActivity::class.java)
        startActivityForResult(intent, 0, ActivityOptions
                .makeSceneTransitionAnimation(requireActivity())
                .toBundle())
    }

    private fun onVersionClicked() {
        versionViewState?.run {
            if (isLatest()) {
                context?.showToast(R.string.settings_version_toast)
            } else {
                Moop.executePlayStore(requireContext())
            }
        }
    }

    companion object {

        fun newInstance(): SettingsFragment = SettingsFragment()
    }
}
