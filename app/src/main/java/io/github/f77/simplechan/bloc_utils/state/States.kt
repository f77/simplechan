package io.github.f77.simplechan.bloc_utils.state

class States {
    companion object {
        fun error(exception: Throwable): ErrorStateInterface {
            return object : ErrorStateInterface {
                override val exception: Throwable
                    get() = exception
            }
        }

        fun initial(): InitialStateInterface = object : InitialStateInterface {}

        fun loading(): LoadingStateInterface = object : LoadingStateInterface {}
    }
}
