package io.github.f77.simplechan.states.boards

import io.github.f77.simplechan.entities.BoardEntity
import io.github.f77.simplechan.states.boards.BoardsState

class BoardsSuccessState(val boards: List<BoardEntity>) : BoardsState() {
}
