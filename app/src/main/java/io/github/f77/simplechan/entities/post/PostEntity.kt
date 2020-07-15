package io.github.f77.simplechan.entities.post

import io.github.f77.simplechan.entities.attachment.AttachmentEntityInterface

class PostEntity(
    val id: String
) {
    var timestamp: Int? = null
    var parentId: String? = null
    var comment: String? = null
    var originalComment: String? = null
    var subject: String? = null
    var ip: String? = null
    var userAgent: String? = null

    var likes: Int? = null
    var dislikes: Int? = null

    val flags: PostFlags = PostFlags()
    val names: PostNames = PostNames()
    val tags: MutableList<String> = mutableListOf()
    val attachments: MutableList<AttachmentEntityInterface> = mutableListOf()
    // icons
}
