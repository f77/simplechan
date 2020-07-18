package io.github.f77.simplechan.bloc_utils.action

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * ActionsLiveData send updates only once.
 * Useful for SnackBar, Navigation, etc.
 *
 * <a href="https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150">LiveData with SnackBar, Navigation and other events (the SingleLiveEvent case)</a>
 */
class ActionsLiveData<T> {
    private val _data: MutableLiveData<ActionWrapper<T>> = MutableLiveData()

    fun setValue(value: T) {
        // Directly post value on the main thread!
        // "If you called method "LiveData.postValue()" multiple times before a main thread executed a posted task,
        // only the last value would be dispatched."
        // @see https://developer.android.com/reference/android/arch/lifecycle/LiveData.html#postValue(T)
        _data.value = ActionWrapper(value)
    }

    fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        _data.observe(owner, makeObserver(observer))
    }

    private fun makeObserver(observer: Observer<in T>): Observer<in ActionWrapper<T>> {
        return Observer { t ->
            t.getContentIfNotHandled()?.let {
                // Only proceed if the event has never been handled
                observer.onChanged(it)
            }
        }
    }
}
