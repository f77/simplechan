package io.github.f77.simplechan.bloc_utils

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.f77.simplechan.bloc_utils.action.ActionsLiveData
import io.github.f77.simplechan.bloc_utils.action.interfaces.ActionInterface
import io.github.f77.simplechan.bloc_utils.event.EventInterface
import io.github.f77.simplechan.bloc_utils.state.StateInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

abstract class BlocViewModel : ViewModel(), BlocViewModelInterface {
    private val _dispatcher: CoroutineDispatcher = Dispatchers.Default
    override val state: MutableLiveData<StateInterface> = MutableLiveData()
    override val actions: ActionsLiveData<ActionInterface> = ActionsLiveData()

    private val debugTagEvent: String = "BLOC_EVENT (" + this::class.simpleName + ")"
    private val debugTagAction: String = "BLOC_ACTION (" + this::class.simpleName + ")"
    private val debugTagState: String = "BLOC_STATE (" + this::class.simpleName + ")"

    /**
     * Directly handling all incoming events.
     * For example, for logging purposes.
     */
    override suspend fun onEvent(event: EventInterface) {}

    /**
     * Add event to the ViewModel.
     * Each event runs in it's own coroutine in the scope of current ViewModel.
     * So, if the model suddenly dies, all running processing of the events will safely stop.
     */
    override fun addEvent(event: EventInterface) {
        viewModelScope.launch(_dispatcher) {
            Log.i(debugTagEvent, event.toString())
            onEvent(event)

            // Handle actions.
            mapEventToAction(event).flowOn(_dispatcher).collect {
                Log.i(debugTagAction, it.toString())

                // Launch in the main thread to receive ALL values.
                // @see https://stackoverflow.com/a/55381715/10452175
                launch(Dispatchers.Main) {
                    actions.setValue(it)
                }
            }

            // Handle states.
            mapEventToState(event).flowOn(_dispatcher).collect {
                Log.i(debugTagState, it.toString())

                // But we don't need to get exactly ALL UI states and can afford to get only last one,
                // so, we can use LiveData.postValue().
                state.postValue(it)
            }
        }
    }
}
