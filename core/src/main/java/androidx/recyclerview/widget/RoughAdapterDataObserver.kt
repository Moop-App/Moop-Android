package androidx.recyclerview.widget

abstract class RoughAdapterDataObserver : RecyclerView.AdapterDataObserver() {

    abstract fun onItemRangeUpdatedRoughly()

    final override fun onChanged() {
        onItemRangeUpdatedRoughly()
    }

    final override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        onItemRangeUpdatedRoughly()
    }

    final override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        onItemRangeUpdatedRoughly()
    }

    final override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        onItemRangeUpdatedRoughly()
    }

    final override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        onItemRangeUpdatedRoughly()
    }

    final override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        onItemRangeUpdatedRoughly()
    }
}
