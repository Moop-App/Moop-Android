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
@file:Suppress("NOTHING_TO_INLINE")

package androidx.recyclerview.widget

import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup

@DslMarker
annotation class SpanSizeLookupDsl

inline fun spanSizeLookup(noinline sizeMapper: (position: Int) -> Int): SpanSizeLookup =
    SpanSizeLookupBuilder(sizeMapper).build()

@SpanSizeLookupDsl
class SpanSizeLookupBuilder(private val spanSize: (position: Int) -> Int) {

    fun build(): SpanSizeLookup {
        return object : SpanSizeLookup() {

            override fun getSpanSize(position: Int): Int {
                return spanSize(position)
            }
        }
    }
}
