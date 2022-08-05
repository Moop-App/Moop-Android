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
package soup.movie.feature.common.ext

import soup.movie.model.Movie

fun Movie.getAgeLabel(): String = when {
    age >= 19 -> "청소년관람불가"
    age >= 15 -> "15세관람가"
    age >= 12 -> "12세관람가"
    age >= 0 -> "전체관람가"
    else -> "등급 미지정"
}
