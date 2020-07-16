package io.github.f77.simplechan.ui.threads

import io.github.f77.simplechan.bloc_utils.BlocViewModel
import io.github.f77.simplechan.bloc_utils.action.interfaces.ActionInterface
import io.github.f77.simplechan.bloc_utils.event.EventInterface
import io.github.f77.simplechan.bloc_utils.event.Events
import io.github.f77.simplechan.bloc_utils.event.InitializedEventInterface
import io.github.f77.simplechan.bloc_utils.state.StateInterface
import io.github.f77.simplechan.events.threads.ThreadsBoardGivenEvent
import io.github.f77.simplechan.states.threads.ThreadsSuccessfulState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ThreadsViewModel : BlocViewModel() {
    private val _textData: String = "placeholder data"

    init {
        // Add initial starting event.
        addEvent(Events.initialized())
    }

    override fun mapEventToAction(event: EventInterface): Flow<ActionInterface> = flow {

    }

    override fun mapEventToState(event: EventInterface): Flow<StateInterface> = flow {
        when (event) {
            is InitializedEventInterface -> {
                println("THREADS INITIALIZED!")
            }
            is ThreadsBoardGivenEvent -> {
                emit(ThreadsSuccessfulState(event.board))
            }
        }
    }
}
