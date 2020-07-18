package io.github.f77.simplechan.entities.attachment

class ImageAttachment(override val url: String) : AttachmentEntityInterface {
    var thumbnail: String? = null
}
