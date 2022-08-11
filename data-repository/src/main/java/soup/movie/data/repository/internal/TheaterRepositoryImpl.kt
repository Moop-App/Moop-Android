/*
 * Copyright 2022 SOUP
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
package soup.movie.data.repository.internal

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import soup.movie.common.IoDispatcher
import soup.movie.data.database.LocalDataSource
import soup.movie.data.network.RemoteDataSource
import soup.movie.data.network.response.asModel
import soup.movie.data.repository.TheaterRepository
import soup.movie.model.TheaterAreaGroup
import javax.inject.Inject

class TheaterRepositoryImpl @Inject constructor(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : TheaterRepository {

    override suspend fun getCodeList(): TheaterAreaGroup {
        return withContext(ioDispatcher) {
            local.getCodeList() ?: remote.getCodeList().asModel().also {
                local.saveCodeList(it)
            }
        }
    }
}