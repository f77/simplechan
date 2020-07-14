package io.github.f77.simplechan.bloc_utils

import androidx.lifecycle.MutableLiveData
import io.github.f77.simplechan.bloc_utils.action.ActionInterface
import io.github.f77.simplechan.bloc_utils.event.EventInterface
import io.github.f77.simplechan.bloc_utils.state.StateInterface
import kotlinx.coroutines.flow.Flow

interface BlocViewModelInterface {
    /**
     * State is a current condition of the UI.
     */
    val state: MutableLiveData<StateInterface>

    /**
     * Actions are like events, but directed from viewModel to UI.
     * For example, navigation events, SnackBar, dialog, etc.
     */
    val actions: MutableLiveData<ActionInterface>

    /**
     * Directly handling all incoming events.
     * For example, for logging purposes.
     */
    suspend fun onEvent(event: EventInterface)

    /**
     * Add event to the ViewModel.
     */
    fun addEvent(event: EventInterface)

    /**
     * Map event to action.
     */
    fun mapEventToAction(event: EventInterface): Flow<ActionInterface>

    /**
     * Map event to UI state.
     */
    fun mapEventToState(event: EventInterface): Flow<StateInterface>
}
