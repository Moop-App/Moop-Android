package soup.widget.recyclerview.snappy

interface SnappyLayoutManager {

    /**
     * @param velocityX
     * @param velocityY
     * @return the resultant position from a fling of the given velocity.
     */
    fun calculateScrollPosition(velocityX: Int, velocityY: Int): Int

    /**
     * @return the position this list must scroll to to fix a state where the
     * views are not snapped to grid.
     */
    fun getFixedScrollPosition(): Int
}
