package io.github.f77.simplechan.repositories

import io.github.f77.simplechan.entities.BoardEntity

interface ImageboardRepositoryInterface {
    fun loadBoards(): List<BoardEntity>
}
