package io.github.f77.simplechan.bloc_utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BlocViewModel<in EventType, StateType> : ViewModel() {
    private val _dispatcher: CoroutineDispatcher = Dispatchers.IO
    val state: MutableLiveData<StateType> = MutableLiveData()

    /**
     * In this function you can handle any event without providing a state.
     * For example, for navigation purposes.
     * onEvent() always process before mapEventToState().
     */
    protected open suspend fun onEvent(event: EventType) {}

    /**
     * In this function you made states as reaction for any events.
     */
    protected abstract fun mapEventToState(event: EventType): Flow<StateType>

    /**
     * Add event to the ViewModel.
     * Each event runs in it's own coroutine in the scope of current ViewModel.
     * So, if the model suddenly dies, all running processing of the events will safely stop.
     */
    open fun addEvent(event: EventType) {
        viewModelScope.launch(_dispatcher) {
            onEvent(event)

            // Take all stages
            mapEventToState(event).collect {
                state.postValue(it)
            }
        }
    }
}
