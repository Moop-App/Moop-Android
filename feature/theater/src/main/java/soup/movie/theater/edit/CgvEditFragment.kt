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
package soup.movie.theater.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import soup.movie.model.Theater
import soup.movie.theater.R
import soup.movie.theater.databinding.TheaterEditChildFragmentBinding

@AndroidEntryPoint
class CgvEditFragment : Fragment(R.layout.theater_edit_child_fragment) {

    private val viewModel: TheaterEditViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(TheaterEditChildFragmentBinding.bind(view)) {
            val listAdapter = TheaterEditChildListAdapter(object : TheaterEditChildListAdapter.Listener {

                override fun add(theater: Theater): Boolean {
                    return viewModel.add(theater)
                }

                override fun remove(theater: Theater) {
                    viewModel.remove(theater)
                }
            })
            listView.adapter = listAdapter
            viewModel.cgvUiModel.observe(viewLifecycleOwner) {
                listAdapter.submitList(it.areaGroupList, it.selectedTheaterIdSet)
            }
        }
    }
}
