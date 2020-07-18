package io.github.f77.simplechan.states.threads

import io.github.f77.simplechan.entities.BoardEntity
import io.github.f77.simplechan.entities.ThreadEntity

class ThreadsSuccessfulState(val board: BoardEntity, val threads: List<ThreadEntity>) : ThreadsState() {
}
