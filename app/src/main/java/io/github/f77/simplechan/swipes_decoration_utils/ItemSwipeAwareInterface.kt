package io.github.f77.simplechan.swipes_decoration_utils

interface ItemSwipeAwareInterface {
    fun onItemSwipedLeft(position: Int)

    fun onItemSwipedRight(position: Int)

    fun onItemMoved(fromPosition: Int, toPosition: Int)
}
