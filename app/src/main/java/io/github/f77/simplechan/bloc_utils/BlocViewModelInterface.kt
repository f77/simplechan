package io.github.f77.simplechan.bloc_utils

import androidx.lifecycle.MutableLiveData
import io.github.f77.simplechan.bloc_utils.action.ActionsLiveData
import io.github.f77.simplechan.bloc_utils.action.interfaces.ActionInterface
import io.github.f77.simplechan.bloc_utils.event.EventInterface
import io.github.f77.simplechan.bloc_utils.state.StateInterface
import kotlinx.coroutines.flow.Flow

interface BlocViewModelInterface : EventsAwareInterface {
    /**
     * State is a current condition of the UI.
     */
    val state: MutableLiveData<StateInterface>

    /**
     * Actions are like events, but directed from viewModel to UI.
     * For example, navigation events, SnackBar, dialog, etc.
     * Actions produce side effects.
     */
    val actions: ActionsLiveData<ActionInterface>

    /**
     * Directly handling all incoming events.
     * For example, for logging purposes.
     */
    suspend fun onEvent(event: EventInterface)

    /**
     * Map event to action.
     */
    fun mapEventToAction(event: EventInterface): Flow<ActionInterface>

    /**
     * Map event to UI state.
     */
    fun mapEventToState(event: EventInterface): Flow<StateInterface>
}
