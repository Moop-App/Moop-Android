@file:Suppress("NOTHING_TO_INLINE")

package androidx.recyclerview.widget

import  androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup

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
