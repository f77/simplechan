package io.github.f77.simplechan.bloc_utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BlocViewModel<in EventType, StateType> : ViewModel(), BlocViewModelInterface<EventType, StateType> {
    private val _dispatcher: CoroutineDispatcher = Dispatchers.IO
    override val state: MutableLiveData<StateType> = MutableLiveData()
    override val actions: MutableLiveData<StateType> = MutableLiveData()

    /**
     * In this function you can handle any event without providing a state.
     * For example, for navigation purposes.
     * onEvent() always process before mapEventToState().
     */
    protected open suspend fun onEvent(event: EventType) {}

    /**
     * Actions are like events, but directed from viewModel to UI.
     * For example, navigation events, SnackBar, dialog, etc.
     */
    protected abstract fun mapEventToAction(event: EventType): Flow<StateType>

    /**
     * State is a current condition of the UI.
     */
    protected abstract fun mapEventToState(event: EventType): Flow<StateType>

    /**
     * Add event to the ViewModel.
     * Each event runs in it's own coroutine in the scope of current ViewModel.
     * So, if the model suddenly dies, all running processing of the events will safely stop.
     */
    override fun addEvent(event: EventType) {
        viewModelScope.launch(_dispatcher) {
            onEvent(event)

            // Handle actions.
            mapEventToAction(event).collect {
                actions.postValue(it)
            }

            // Handle states.
            mapEventToState(event).collect {
                state.postValue(it)
            }
        }
    }
}
