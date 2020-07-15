package io.github.f77.simplechan.repositories

import io.github.f77.simplechan.entities.BoardEntity
import io.github.f77.simplechan.entities.ThreadEntity

interface ImageboardRepositoryInterface : ImageboardRepositoryConfigurationInterface {
    val name: String

    val defaultDomains: List<String>

    fun loadBoards(): List<BoardEntity>

    fun loadThreads(board: BoardEntity, pageNumbers: List<Int>): List<ThreadEntity>
}
