package soup.widget.recyclerview.listener

interface OnItemDismissListener {

    /**
     * Called when an item has been dismissed by a swipe.
     *
     * @param position The position of the item dismissed.
     */
    fun onItemDismiss(position: Int)
}
