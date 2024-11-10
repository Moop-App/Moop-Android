/*
 * Copyright 2024 SOUP
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
package soup.movie.core.designsystem.tools

import androidx.compose.ui.tooling.preview.Preview

/**
 * Multipreview annotation that represents various device sizes.
 * Add this annotation to a composable to render various devices.
 */
@Preview(name = "phone", widthDp = 360, heightDp = 640)
@Preview(name = "landscape", widthDp = 640, heightDp = 360)
@Preview(name = "foldable", device = "id:pixel_fold")
@Preview(name = "tablet", device = "id:pixel_tablet")
annotation class DevicePreviews
