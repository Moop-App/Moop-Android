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
package soup.movie.data.api.response

import kotlinx.serialization.Serializable
import soup.movie.model.BoxOffice

/**
 * @param rank 전일 순위
 * @param audiCnt 전일 관객수
 * @param audiAcc 누적 관객수
 */
@Serializable
class BoxOfficeResponse(
    val rank: Int,
    val audiCnt: Int,
    val audiAcc: Int,
)

fun BoxOfficeResponse.asModel(): BoxOffice {
    return BoxOffice(
        rank = rank,
        audiCnt = audiCnt,
        audiAcc = audiAcc,
    )
}
