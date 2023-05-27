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
package soup.movie.feature.theatermap.internal

sealed class TheaterMarkerUiModel {
    abstract val areaCode: String
    abstract val code: String
    abstract val name: String
    abstract val lng: Double
    abstract val lat: Double
}

internal class CgvMarkerUiModel(
    override val areaCode: String,
    override val code: String,
    override val name: String,
    override val lng: Double,
    override val lat: Double
) : TheaterMarkerUiModel()

internal class LotteCinemaMarkerUiModel(
    override val areaCode: String,
    override val code: String,
    override val name: String,
    override val lng: Double,
    override val lat: Double
) : TheaterMarkerUiModel()

internal class MegaboxMarkerUiModel(
    override val areaCode: String,
    override val code: String,
    override val name: String,
    override val lng: Double,
    override val lat: Double
) : TheaterMarkerUiModel()
