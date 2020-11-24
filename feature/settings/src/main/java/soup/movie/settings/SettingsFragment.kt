package soup.movie.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.SharedElementCallback
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import soup.movie.ext.showToast
import soup.movie.ext.startActivitySafely
import soup.movie.model.Theater
import soup.movie.settings.databinding.SettingsFragmentBinding
import soup.movie.settings.databinding.SettingsItemTheaterBinding
import soup.movie.settings.databinding.SettingsItemVersionBinding
import soup.movie.system.SystemViewModel
import soup.movie.theme.setThemeOptionLabel
import soup.movie.util.*

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.settings_fragment) {

    private var binding: SettingsFragmentBinding by autoCleared()

    private val systemViewModel: SystemViewModel by activityViewModels()
    private val viewModel: SettingsViewModel by viewModels()

    private var versionViewState: VersionSettingUiModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String>,
                                             sharedElements: MutableMap<String, View>) {
                sharedElements.clear()
                names.forEach { name ->
                    binding.theaterItem.theaterGroup.findViewWithTag<View>(name)?.let {
                        sharedElements[name] = it
                    }
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SettingsFragmentBinding.bind(view).apply {
            initViewState(viewModel)
            adaptSystemWindowInset()
        }
    }

    private fun SettingsFragmentBinding.adaptSystemWindowInset() {
        Insetter.builder()
            .setOnApplyInsetsListener { settingsScene, insets, initialState ->
                settingsScene.updatePadding(
                    top = initialState.paddings.top + insets.systemWindowInsetTop
                )
            }
            .applyToView(settingsScene)
        Insetter.builder()
            .setOnApplyInsetsListener { listView, insets, initialState ->
                listView.updatePadding(
                    bottom = initialState.paddings.bottom + insets.systemWindowInsetBottom
                )
            }
            .applyToView(listView)
    }

    private fun SettingsFragmentBinding.initViewState(viewModel: SettingsViewModel) {
        toolbar.setNavigationOnClickListener {
            systemViewModel.openNavigationMenu()
        }
        theaterItem.editTheaterButton.setOnDebounceClickListener {
            onTheaterEditClicked()
        }
        viewModel.theaterUiModel.observe(viewLifecycleOwner) {
            theaterItem.render(it)
        }

        themeItem.editThemeButton.setOnDebounceClickListener {
            onThemeEditClicked()
        }
        themeItem.themeName.setOnDebounceClickListener {
            onThemeEditClicked()
        }
        viewModel.themeUiModel.observe(viewLifecycleOwner) {
            themeItem.themeName.setThemeOptionLabel(it.themeOption)
        }

        //TODO: Apply theater mode
        //tmPrepare.setOnClickListener {
        //    startActivity(Intent(requireContext(), TheaterModeTileActivity::class.java))
        //}
        feedbackItem.bugReportButton.setOnDebounceClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(HELP_EMAIL))
            intent.putExtra(Intent.EXTRA_SUBJECT, "뭅 v${BuildConfig.VERSION_NAME} 버그리포트")
            it.context.startActivitySafely(intent)
        }
        versionItem.versionButton.setOnDebounceClickListener {
            onVersionClicked()
        }
        versionItem.marketIcon.setOnDebounceClickListener {
            Moop.executePlayStore(it.context)
        }
        viewModel.versionUiModel.observe(viewLifecycleOwner) {
            versionItem.render(it)
        }
    }

    private fun SettingsItemTheaterBinding.render(viewState: TheaterSettingUiModel) {
        val theaters = viewState.theaterList
        noTheaterView.isVisible = theaters.isEmpty()
        theaterGroup.isVisible = theaters.isNotEmpty()
        theaterGroup.run {
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

    @LayoutRes
    private fun Theater.getChipLayout(): Int {
        return when(type) {
            Theater.TYPE_CGV -> R.layout.chip_action_cgv
            Theater.TYPE_LOTTE -> R.layout.chip_action_lotte
            Theater.TYPE_MEGABOX -> R.layout.chip_action_megabox
            else -> throw IllegalArgumentException("$type is not valid type.")
        }
    }

    private fun Theater.executeWeb(ctx: Context) {
        return when (type) {
            Theater.TYPE_CGV -> Cgv.executeWeb(ctx, this)
            Theater.TYPE_LOTTE -> LotteCinema.executeWeb(ctx, this)
            Theater.TYPE_MEGABOX -> Megabox.executeWeb(ctx, this)
            else -> throw IllegalArgumentException("$type is not valid type.")
        }
    }

    private fun SettingsItemVersionBinding.render(uiModel: VersionSettingUiModel) {
        versionViewState = uiModel
        currentVersionLabel.text = if (uiModel.isLatest) {
            getString(R.string.settings_version_latest, uiModel.versionName)
        } else {
            getString(R.string.settings_version_current, uiModel.versionName)
        }
        if (uiModel.isLatest) {
            marketIcon.setImageResource(R.drawable.ic_round_shop)
        } else {
            marketIcon.setImageResource(R.drawable.ic_round_new_releases)
        }
        if (uiModel.isLatest.not()) {
            AlertDialog.Builder(requireContext())
                .setIcon(R.drawable.ic_round_new_releases)
                .setTitle(R.string.settings_version_update_title)
                .setMessage(getString(R.string.settings_version_update_message))
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
        binding.theaterItem.theaterGroup.run {
            (0 until childCount)
                .mapNotNull { getChildAt(it) }
                .map { it to it.transitionName }
                .toTypedArray()
        }

    private fun onThemeEditClicked() {
        findNavController().navigate(
            SettingsFragmentDirections.actionToThemeOption())
    }

    private fun onVersionClicked() {
        versionViewState?.run {
            if (isLatest) {
                context?.showToast(R.string.settings_version_toast)
            } else {
                Moop.executePlayStore(requireContext())
            }
        }
    }

    companion object {

        private const val HELP_EMAIL = "help.moop@gmail.com"
    }
}
