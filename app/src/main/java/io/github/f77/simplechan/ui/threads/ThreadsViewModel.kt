package io.github.f77.simplechan.ui.threads

import io.github.f77.simplechan.actions.main_activity.TitleUpdatedAction
import io.github.f77.simplechan.actions.threads.ThreadInformationSelectedAction
import io.github.f77.simplechan.bloc_utils.BlocViewModel
import io.github.f77.simplechan.bloc_utils.action.interfaces.ActionInterface
import io.github.f77.simplechan.bloc_utils.action.interfaces.Actions
import io.github.f77.simplechan.bloc_utils.event.*
import io.github.f77.simplechan.bloc_utils.state.StateInterface
import io.github.f77.simplechan.bloc_utils.state.States
import io.github.f77.simplechan.entities.ThreadEntity
import io.github.f77.simplechan.events.threads.ThreadClosedEvent
import io.github.f77.simplechan.events.threads.ThreadImageClicked
import io.github.f77.simplechan.events.threads.ThreadInformationClickedEvent
import io.github.f77.simplechan.events.threads.ThreadsBoardGivenEvent
import io.github.f77.simplechan.repositories.DvachRepository
import io.github.f77.simplechan.repositories.ImageboardRepositoryInterface
import io.github.f77.simplechan.states.threads.ThreadsSuccessfulState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ThreadsViewModel : BlocViewModel() {
    private val _imageboardRepository: ImageboardRepositoryInterface = DvachRepository()

    // Data source. Model just controls data, but not keep them.
    // Data may be stored in the database.
    private val _threads: MutableList<ThreadEntity> = mutableListOf()

    init {
        // Add initial starting event.
        addEvent(Events.initialized())
    }

    override fun mapEventToAction(event: EventInterface): Flow<ActionInterface> = flow {
        when (event) {
            is ItemClickedEventInterface -> {
                emit(Actions.simpleSnackBar("CLICKED ON " + event.position))
            }
            is ItemLongClickedEventInterface -> {
                emit(Actions.simpleSnackBar("LONG CLICKED ON " + event.position))
            }
            is ThreadInformationClickedEvent -> {
                emit(Actions.simpleSnackBar("THREAD INFORMATION CLICKED ON " + event.position))
                emit(ThreadInformationSelectedAction(event.position))
            }
            is ThreadImageClicked -> {
                emit(Actions.simpleSnackBar("THREAD IMAGE CLICKED ON " + event.position))
            }
            is ThreadsBoardGivenEvent -> {
                var title = "/" + event.board.id + "/"
                event.board.name?.let { title += " — $it" }

                // Immediately update the title, before any network requests.
                emit(TitleUpdatedAction(title))
            }
            is ThreadClosedEvent -> {
                // @TODO: Temporary solution! Model must not have its own state!
                _threads.clear()
            }
        }
    }

    override fun mapEventToState(event: EventInterface): Flow<StateInterface> = flow {
        when (event) {
            is InitializedEventInterface -> {
                // Send loading state
                emit(States.loading())
            }
            is ThreadsBoardGivenEvent -> {
                // Just call heavy synchronous network function.
                try {
                    // Update data.
                    val newThreads = _imageboardRepository.loadThreads(event.board, listOf(1))
                    _threads.clear()
                    _threads.addAll(newThreads)
                    emit(ThreadsSuccessfulState(event.board, _threads))
                } catch (e: Throwable) {
                    emit(States.error(e))
                }
            }
        }
    }
}
