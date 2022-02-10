/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soup.movie.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.dynamicfeatures.fragment.ui.AbstractProgressFragment
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.android.material.composethemeadapter.MdcTheme
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import soup.compose.visibility.invisible
import soup.movie.BuildConfig
import soup.movie.R
import soup.movie.util.LauncherIcons
import timber.log.Timber

class CustomProgressFragment : AbstractProgressFragment() {

    private sealed class State {
        data class Progress(val bytesDownloaded: Long, val bytesTotal: Long) : State()
        object Cancelled : State()
        object Failed : State()
    }

    private var state by mutableStateOf<State>(State.Progress(0, 0))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MdcTheme {
                    CustomProgressScreen(state)
                }
            }
        }
    }

    override fun onProgress(
        @SplitInstallSessionStatus status: Int,
        bytesDownloaded: Long,
        bytesTotal: Long
    ) {
        state = State.Progress(bytesDownloaded, bytesTotal)
    }

    override fun onCancelled() {
        state = State.Cancelled
    }

    override fun onFailed(@SplitInstallErrorCode errorCode: Int) {
        Timber.w("Installation failed with error $errorCode")
        state = State.Failed
    }

    @Composable
    private fun CustomProgressScreen(state: State) {
        ProvideWindowInsets {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                if (state is State.Progress) {
                    ProgressAnimation(
                        modifier = Modifier
                            .navigationBarsPadding(start = false, end = false)
                            .padding(end = 8.dp, bottom = 160.dp)
                            .size(width = 256.dp, height = 300.dp)
                    )
                }
                CustomProgressContents(
                    state = state,
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding(start = false, end = false, bottom = false)
                        .padding(horizontal = 32.dp)
                )
                Image(
                    painterResource(id = R.drawable.ic_splash_launcher),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .navigationBarsPadding(start = false, end = false)
                        .padding(16.dp)
                        .size(width = 100.dp, height = 40.dp)
                        .align(Alignment.BottomEnd)
                )
            }
        }
    }

    @Composable
    private fun ProgressAnimation(modifier: Modifier = Modifier) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.dfm_loading))
        val progress by animateLottieCompositionAsState(
            composition,
            isPlaying = true,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = modifier
        )
    }

    @Composable
    private fun CustomProgressContents(state: State, modifier: Modifier = Modifier) {
        val context = LocalContext.current
        val appIcon = remember(context) {
            LauncherIcons(context).getAppIcon(context, BuildConfig.APPLICATION_ID)
        }
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberDrawablePainter(appIcon),
                contentDescription = null,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            remember(state) {
                when (state) {
                    State.Cancelled -> R.string.installation_cancelled
                    State.Failed -> R.string.installation_failed
                    is State.Progress -> null
                }
            }?.let { id ->
                Text(
                    text = stringResource(id),
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            val (indeterminate, progress) = if (state is State.Progress) {
                if (state.bytesTotal == 0L) {
                    true to 0f
                } else {
                    false to state.bytesDownloaded / state.bytesTotal.toFloat()
                }
            } else {
                true to 0f
            }
            LinearProgressIndicator(
                indeterminate = indeterminate,
                progress = progress,
                color = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .invisible(state !is State.Progress)
                    .fillMaxWidth()
                    .padding(vertical = 14.dp)
            )

            if (state == State.Cancelled) {
                Button(
                    onClick = { navigate() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                ) {
                    Text(text = stringResource(R.string.retry))
                }
            } else if (state == State.Failed) {
                Button(
                    onClick = { findNavController().popBackStack() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                ) {
                    Text(text = stringResource(R.string.ok))
                }
            }
        }
    }

    @Composable
    private fun LinearProgressIndicator(
        progress: Float,
        indeterminate: Boolean,
        modifier: Modifier = Modifier,
        color: Color = MaterialTheme.colors.secondary
    ) {
        if (indeterminate) {
            LinearProgressIndicator(
                color = color,
                modifier = modifier
            )
        } else {
            LinearProgressIndicator(
                progress = progress,
                color = color,
                modifier = modifier
            )
        }
    }
}
