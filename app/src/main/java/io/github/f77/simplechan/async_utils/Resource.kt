package io.github.f77.simplechan.async_utils

/**
 * A generic class that holds a value with its loading status.
 *
 * @see <a href="https://github.com/android/architecture-components-samples/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/vo/Resource.kt">Sample apps for Android Architecture Components</a>
 */
data class Resource<out T>(val status: Status, val data: T?, val exception: Throwable?) {
    enum class Status {
        LOADING,
        SUCCESS,
        ERROR,
    }

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(exception: Throwable): Resource<T> {
            return Resource(Status.ERROR, null, exception)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null)
        }
    }
}
