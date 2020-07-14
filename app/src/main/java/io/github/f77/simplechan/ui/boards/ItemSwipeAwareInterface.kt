package io.github.f77.simplechan.ui.boards

interface ItemSwipeAwareInterface {
    fun onItemSwipedLeft(position: Int)

    fun onItemSwipedRight(position: Int)

    fun onItemMoved(fromPosition: Int, toPosition: Int)
}
