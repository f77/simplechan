package io.github.f77.simplechan.ui.boards

import io.github.f77.simplechan.bloc_utils.BlocViewModel
import io.github.f77.simplechan.bloc_utils.action.interfaces.ActionInterface
import io.github.f77.simplechan.bloc_utils.action.interfaces.Actions
import io.github.f77.simplechan.bloc_utils.event.*
import io.github.f77.simplechan.bloc_utils.state.StateInterface
import io.github.f77.simplechan.bloc_utils.state.States
import io.github.f77.simplechan.entities.BoardEntity
import io.github.f77.simplechan.repositories.DvachRepository
import io.github.f77.simplechan.repositories.ImageboardRepositoryInterface
import io.github.f77.simplechan.states.BoardsSuccessState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class BoardsViewModel : BlocViewModel() {
    private val _imageboardRepository: ImageboardRepositoryInterface = DvachRepository()

    // Data source. Model just controls data, but not keep them.
    // Data may be stored in the database.
    private val _boards: MutableList<BoardEntity> = mutableListOf()

    init {
        // Add initial starting event.
        addEvent(object : InitializedEventInterface {})
    }

    /**
     * Log all events.
     */
    override suspend fun onEvent(event: EventInterface) {
        super.onEvent(event)
        println("EVENT_FIRED: " + event::class)
    }

    /**
     * Actions are like events, but directed from viewModel to UI.
     * For example, navigation events, SnackBar, dialog, etc.
     */
    override fun mapEventToAction(event: EventInterface): Flow<ActionInterface> = flow {
        when (event) {
            is ItemClickedEventInterface -> {
                emit(Actions.navigate(event.position, 0))
            }
            is ItemLongClickedEventInterface -> {
                emit(Actions.simpleSnackBar("LONG CLICKED ON " + event.position))
            }
            is ItemSwipedLeftEventInterface -> {
                _boards.removeAt(event.position)

                emit(Actions.itemRemoved(event.position))
            }
            is ItemSwipedRightEventInterface -> {
                _boards[event.position].name = "edited!"

                emit(Actions.itemChanged(event.position))
            }
            is ItemMovedEventInterface -> {
                if (event.fromPosition < event.toPosition) {
                    for (i in event.fromPosition until event.toPosition) {
                        Collections.swap(_boards, i, i + 1)
                    }
                } else {
                    for (i in event.fromPosition downTo event.toPosition + 1) {
                        Collections.swap(_boards, i, i - 1)
                    }
                }

                emit(Actions.itemMoved(event.fromPosition, event.toPosition))
            }
        }
    }

    /**
     * State is a current condition of the UI.
     */
    override fun mapEventToState(event: EventInterface): Flow<StateInterface> = flow {
        when (event) {
            is InitializedEventInterface -> {
                // Send loading state
                emit(States.loading())

                // Just call heavy synchronous network function.
                try {
                    // Update data.
                    val newBoards = _imageboardRepository.loadBoards()
                    _boards.clear()
                    _boards.addAll(newBoards)
                    emit(BoardsSuccessState(_boards))
                } catch (e: Throwable) {
                    emit(States.error(e))
                }
            }
        }
    }
}
