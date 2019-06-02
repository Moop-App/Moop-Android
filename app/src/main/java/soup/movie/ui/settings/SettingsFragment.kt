package soup.movie.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.SharedElementCallback
import androidx.core.view.isVisible
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.item_settings_feedback.*
import kotlinx.android.synthetic.main.item_settings_theater.*
import kotlinx.android.synthetic.main.item_settings_theater_mode.*
import kotlinx.android.synthetic.main.item_settings_theme.*
import kotlinx.android.synthetic.main.item_settings_version.*
import soup.movie.BuildConfig
import soup.movie.R
import soup.movie.analytics.EventAnalytics
import soup.movie.util.helper.Moop
import soup.movie.data.helper.executeWeb
import soup.movie.data.helper.getChipLayout
import soup.movie.databinding.SettingsFragmentBinding
import soup.movie.ui.BaseFragment
import soup.movie.util.*
import javax.inject.Inject

class SettingsFragment : BaseFragment() {

    private val viewModel: SettingsViewModel by viewModel()

    @Inject
    lateinit var analytics: EventAnalytics

    private var versionViewState: VersionSettingUiModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String>,
                                             sharedElements: MutableMap<String, View>) {
                sharedElements.clear()
                names.forEach { name ->
                    theaterGroup.findViewWithTag<View>(name)?.let {
                        sharedElements[name] = it
                    }
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return SettingsFragmentBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = this@SettingsFragment.viewModel
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewState()
        viewModel.theaterUiModel.observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.versionUiModel.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun initViewState() {
        editTheaterButton.setOnDebounceClickListener {
            onTheaterEditClicked()
        }
        editThemeButton.setOnDebounceClickListener {
            onThemeEditClicked()
        }
        themeName.setOnDebounceClickListener {
            onThemeEditClicked()
        }
        tmPrepare.setOnClickListener {
            //TODO: Apply theater mode
            //startActivity(Intent(requireContext(), TheaterModeTileActivity::class.java))
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
        marketIcon.setOnDebounceClickListener {
            Moop.executePlayStore(requireContext())
        }
    }

    private fun render(viewState: TheaterSettingUiModel) {
        val theaters = viewState.theaterList
        noTheaterView?.isVisible = theaters.isEmpty()
        theaterGroup?.isVisible = theaters.isNotEmpty()
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

    private fun render(viewState: VersionSettingUiModel) {
        versionViewState = viewState
        currentVersionLabel?.text = getString(R.string.settings_version_current, viewState.current.versionName)
        latestVersionLabel?.text = getString(R.string.settings_version_latest, viewState.latest.versionName)
        if (viewState.isLatest()) {
            marketIcon?.setImageResource(R.drawable.ic_round_shop)
        } else {
            marketIcon?.setImageResource(R.drawable.ic_round_new_releases)
        }
        if (viewState.isLatest().not()) {
            AlertDialog.Builder(requireContext())
                .setIcon(R.drawable.ic_round_new_releases)
                .setTitle(R.string.settings_version_update_title)
                .setMessage(getString(R.string.settings_version_update_message, viewState.latest.versionName))
                .setPositiveButton(R.string.settings_version_update_button_positive) { _, _ ->
                    Moop.executePlayStore(requireContext())
                }
                .setNegativeButton(R.string.settings_version_update_button_negative) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun onTheaterEditClicked() {
        findNavController().navigate(
            SettingsFragmentDirections.actionToTheaterSort(),
            FragmentNavigatorExtras(*createSharedElementsForTheaters())
        )
    }

    private fun createSharedElementsForTheaters(): Array<Pair<View, String>> =
        theaterGroup?.run {
            (0 until childCount)
                .mapNotNull { getChildAt(it) }
                .map { it to it.transitionName }
                .toTypedArray()
        } ?: emptyArray()

    private fun onThemeEditClicked() {
        findNavController().navigate(
            SettingsFragmentDirections.actionToThemeOption())
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
}
