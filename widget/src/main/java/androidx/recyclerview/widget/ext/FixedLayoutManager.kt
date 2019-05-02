package androidx.recyclerview.widget.ext

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager

class FixedLayoutManager : LinearLayoutManager {

    constructor(context: Context) : super(context)

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun canScrollHorizontally(): Boolean = false

    override fun canScrollVertically(): Boolean = false
}
