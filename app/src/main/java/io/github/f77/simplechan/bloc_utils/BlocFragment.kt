package io.github.f77.simplechan.bloc_utils

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import io.github.f77.simplechan.actions.main_activity.TitleUpdatedAction
import io.github.f77.simplechan.bloc_utils.action.interfaces.ActionInterface
import io.github.f77.simplechan.bloc_utils.action.interfaces.SimpleSnackBarActionInterface
import io.github.f77.simplechan.bloc_utils.event.EventInterface
import io.github.f77.simplechan.bloc_utils.event.Events
import io.github.f77.simplechan.bloc_utils.state.StateInterface
import io.github.f77.simplechan.events.main_activity.TitleUpdatedEvent
import io.github.f77.simplechan.ui.main_activity.MainActivityViewModel


abstract class BlocFragment : Fragment() {
    protected abstract val viewModel: BlocViewModelInterface
    protected lateinit var rootView: View

    private val _mainActivityViewModel: MainActivityViewModel by activityViewModels()

    abstract fun observeActions(): Observer<ActionInterface>
    abstract fun observeStates(): Observer<StateInterface>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        viewModel.actions.observe(viewLifecycleOwner, realActionsObserver())
        viewModel.state.observe(viewLifecycleOwner, observeStates())
    }

    /**
     * Some actions from fragment viewModel should to be forwarded into the main activity's viewModel.
     * @return Was the event forwarded to the main activity's viewModel?
     */
    protected open fun resendActionsToMainActivityViewModel(action: ActionInterface): Boolean {
        var event: EventInterface? = null
        when (action) {
            is TitleUpdatedAction -> {
                event = TitleUpdatedEvent(action.title)
            }
            is SimpleSnackBarActionInterface -> {
                event = Events.simpleSnackBar(action.message)
            }
        }

        event?.let {
            _mainActivityViewModel.addEvent(event)
        }
        return (event !== null)
    }

    private fun realActionsObserver(): Observer<ActionInterface> = Observer {
        if (!resendActionsToMainActivityViewModel(it)) {
            observeActions().onChanged(it)
        }
    }
}
