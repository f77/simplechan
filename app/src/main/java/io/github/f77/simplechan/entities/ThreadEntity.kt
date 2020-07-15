package io.github.f77.simplechan.entities

import io.github.f77.simplechan.entities.post.PostEntity

class ThreadEntity(
    /**
     * Thread number.
     */
    val id: String,
    val board: BoardEntity,
    val opPost: PostEntity,
    val lastPosts: List<PostEntity>
) {
    var totalPostsCount: Int? = null
    var totalFilesCount: Int? = null
}
