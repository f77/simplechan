package io.github.f77.simplechan.bloc_utils.action

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 * @see <a href="https://gist.github.com/JoseAlcerreca/5b661f1800e1e654f07cc54fe87441af#file-event-kt">EventWrapper</a>
 */
open class ActionWrapper<out T>(private val content: T) {
    private var _hasBeenHandled = false

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (_hasBeenHandled) {
            null
        } else {
            _hasBeenHandled = true
            content
        }
    }
}
