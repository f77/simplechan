package io.github.f77.simplechan.ui.boards

import io.github.f77.simplechan.bloc_utils.BlocViewModel
import io.github.f77.simplechan.bloc_utils.action.ItemChangedActionInterface
import io.github.f77.simplechan.bloc_utils.action.ItemMovedActionInterface
import io.github.f77.simplechan.bloc_utils.action.ItemRemovedActionInterface
import io.github.f77.simplechan.bloc_utils.event.InitializedEventInterface
import io.github.f77.simplechan.bloc_utils.event.ItemMovedEventInterface
import io.github.f77.simplechan.bloc_utils.event.ItemSwipedLeftEventInterface
import io.github.f77.simplechan.bloc_utils.event.ItemSwipedRightEventInterface
import io.github.f77.simplechan.bloc_utils.state.ErrorStateInterface
import io.github.f77.simplechan.bloc_utils.state.LoadingStateInterface
import io.github.f77.simplechan.entities.BoardEntity
import io.github.f77.simplechan.events.BoardsEvent
import io.github.f77.simplechan.repositories.DvachRepository
import io.github.f77.simplechan.repositories.ImageboardRepositoryInterface
import io.github.f77.simplechan.states.BoardsState
import io.github.f77.simplechan.states.BoardsSuccessState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class BoardsViewModel : BlocViewModel<BoardsEvent, BoardsState>(), ItemSwipeAwareInterface {
    private val _imageboardRepository: ImageboardRepositoryInterface = DvachRepository()

    // Data source. Model just controls data, but not keep them.
    private val _boards: MutableList<BoardEntity> = mutableListOf()

    init {
        // Add initial starting event.
        addEvent(object : BoardsEvent(), InitializedEventInterface {})
    }

    /**
     * Actions are like events, but directed from viewModel to UI.
     * For example, navigation events, SnackBar, dialog, etc.
     */
    override fun mapEventToAction(event: BoardsEvent): Flow<BoardsState> = flow {
        when (event) {
            is ItemSwipedLeftEventInterface -> {
                _boards.removeAt(event.position)

                emit(object : BoardsState(), ItemRemovedActionInterface {
                    override val position: Int
                        get() = event.position
                })
            }
            is ItemSwipedRightEventInterface -> {
                _boards[event.position].name = "edited!"

                emit(object : BoardsState(), ItemChangedActionInterface {
                    override val position: Int
                        get() = event.position
                })
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

                emit(object : BoardsState(), ItemMovedActionInterface {
                    override val fromPosition: Int
                        get() = event.fromPosition
                    override val toPosition: Int
                        get() = event.toPosition
                })
            }
        }
    }

    /**
     * State is a current condition of the UI.
     */
    override fun mapEventToState(event: BoardsEvent): Flow<BoardsState> = flow {
        when (event) {
            is InitializedEventInterface -> {
                // Update data.

                // Send loading state
                emit(object : BoardsState(), LoadingStateInterface {})

                // Just call heavy synchronous network function.
                try {
                    val newBoards = _imageboardRepository.loadBoards()
                    _boards.clear()
                    _boards.addAll(newBoards)
                    emit(BoardsSuccessState(_boards))
                } catch (e: Throwable) {
                    emit(object : BoardsState(), ErrorStateInterface {
                        override val exception: Throwable
                            get() = e
                    })
                }
            }
        }
    }

    override fun onItemSwipedLeft(position: Int) {
        addEvent(object : BoardsEvent(), ItemSwipedLeftEventInterface {
            override val position: Int
                get() = position
        })
    }

    override fun onItemSwipedRight(position: Int) {
        addEvent(object : BoardsEvent(), ItemSwipedRightEventInterface {
            override val position: Int
                get() = position
        })
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        addEvent(object : BoardsEvent(), ItemMovedEventInterface {
            override val fromPosition: Int
                get() = fromPosition
            override val toPosition: Int
                get() = toPosition
        })
    }
}
