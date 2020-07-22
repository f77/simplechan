package io.github.f77.simplechan.bloc_utils

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
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
    protected lateinit var rootViewGroup: ViewGroup

    private val _mainActivityViewModel: MainActivityViewModel by activityViewModels()

    /**
     * Find and initialize all needed views here.
     */
    protected abstract fun initViews(rootView: View)

    /**
     * Handle given action.
     */
    protected abstract fun handleAction(action: ActionInterface)

    /**
     * Render given state.
     */
    protected abstract fun render(state: StateInterface)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.actions.observe(viewLifecycleOwner, realActionsObserver())
        viewModel.state.observe(viewLifecycleOwner, Observer {
            render(it)
        })

        // Initialize views.
        rootViewGroup = view as ViewGroup
        initViews(view)
    }

    /**
     * Some actions should to be forwarded from fragment's ViewModel to the main activity's ViewModel.
     */
    protected open fun mapActionToMainActivityEvent(action: ActionInterface): EventInterface? {
        when (action) {
            is TitleUpdatedAction -> {
                return TitleUpdatedEvent(action.title)
            }
            is SimpleSnackBarActionInterface -> {
                return Events.simpleSnackBar(action.message)
            }
        }

        return null
    }

    private fun realActionsObserver(): Observer<ActionInterface> = Observer {
        val mainActivityEvent = mapActionToMainActivityEvent(it)

        if (mainActivityEvent === null) {
            handleAction(it)
        } else {
            _mainActivityViewModel.addEvent(mainActivityEvent)
        }
    }
}
