package io.github.f77.simplechan.bloc_utils

import io.github.f77.simplechan.bloc_utils.event.EventInterface

interface EventsAwareInterface {
    /**
     * Add event to the ViewModel.
     */
    fun addEvent(event: EventInterface)
}
