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
package soup.movie.theatermap

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.EntryPointAccessors
import soup.movie.data.repository.MovieRepository
import soup.movie.di.TheaterMapModuleDependencies
import soup.movie.ext.lazyFast
import soup.movie.theatermap.di.DaggerTheaterMapComponent
import soup.movie.theatermap.internal.TheaterMapScreen
import soup.movie.theatermap.internal.TheaterMapViewModel
import soup.movie.ui.MovieTheme
import soup.movie.util.viewModelProviderFactoryOf
import javax.inject.Inject

class TheaterMapFragment : Fragment() {

    @Inject
    lateinit var repository: MovieRepository

    private val viewModel: TheaterMapViewModel by viewModels {
        viewModelProviderFactoryOf { TheaterMapViewModel(repository) }
    }

    private val locationSource by lazyFast {
        FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerTheaterMapComponent.builder()
            .context(context)
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    context.applicationContext,
                    TheaterMapModuleDependencies::class.java
                )
            )
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MovieTheme {
                    TheaterMapScreen(
                        viewModel = viewModel,
                        locationSource = locationSource,
                        onNavigationOnClick = {
                            findNavController().navigateUp()
                        },
                    )
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}
