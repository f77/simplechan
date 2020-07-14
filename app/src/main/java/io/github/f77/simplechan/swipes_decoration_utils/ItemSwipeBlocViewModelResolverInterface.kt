package io.github.f77.simplechan.swipes_decoration_utils

import io.github.f77.simplechan.bloc_utils.BlocViewModelInterface
import io.github.f77.simplechan.bloc_utils.event.Events

interface ItemSwipeBlocViewModelResolverInterface : ItemSwipeAwareInterface, BlocViewModelInterface {
    override fun onItemSwipedLeft(position: Int) {
        addEvent(Events.itemSwipedLeft(position))
    }

    override fun onItemSwipedRight(position: Int) {
        addEvent(Events.itemSwipedRight(position))
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        addEvent(Events.itemMoved(fromPosition, toPosition))
    }
}
