package io.github.f77.simplechan.ui.gallery

import io.github.f77.simplechan.bloc_utils.BlocViewModel
import io.github.f77.simplechan.bloc_utils.event.InitializedEventInterface
import io.github.f77.simplechan.bloc_utils.state.ErrorStateInterface
import io.github.f77.simplechan.bloc_utils.state.LoadingStateInterface
import io.github.f77.simplechan.events.BoardsEvent
import io.github.f77.simplechan.repositories.DvachRepository
import io.github.f77.simplechan.repositories.ImageboardRepositoryInterface
import io.github.f77.simplechan.states.BoardsState
import io.github.f77.simplechan.states.BoardsSuccessState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GalleryViewModel : BlocViewModel<BoardsEvent, BoardsState>() {
    private val _imageboardRepository: ImageboardRepositoryInterface = DvachRepository()

    init {
        addEvent(object : BoardsEvent(), InitializedEventInterface {})
    }

    override suspend fun onEvent(event: BoardsEvent) {

    }

    override fun mapEventToState(event: BoardsEvent): Flow<BoardsState> = flow {
        if (event is InitializedEventInterface) {
            println("BOARDS INITIALIZED!")

            // Send loading state
            emit(object : BoardsState(), LoadingStateInterface {})

            // Just call heavy synchronous network function.
            try {
                val boards = _imageboardRepository.loadBoards()
                emit(BoardsSuccessState(boards))
            } catch (e: Throwable) {
                emit(object : BoardsState(), ErrorStateInterface {
                    override val exception: Throwable
                        get() = e
                })
            }
        }
    }
}
