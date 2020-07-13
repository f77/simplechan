package io.github.f77.simplechan.entities

class BoardEntity(
    /**
     * BoardID. "b", "int", "a", etc.
     */
    val id: String
) {
    /**
     * Visible name. "International", "Anime", etc.
     */
    var name: String? = null

    var categoryName: String? = null
    var defaultUserName: String? = null
    var speed: Int? = null
    var bumpLimit: Int? = null
    var maxCommentSizeBytes: Int? = null
}
