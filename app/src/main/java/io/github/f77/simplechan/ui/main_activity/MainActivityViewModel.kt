package io.github.f77.simplechan.ui.main_activity

import io.github.f77.simplechan.actions.main_activity.TitleUpdatedAction
import io.github.f77.simplechan.bloc_utils.BlocViewModel
import io.github.f77.simplechan.bloc_utils.action.interfaces.ActionInterface
import io.github.f77.simplechan.bloc_utils.action.interfaces.Actions
import io.github.f77.simplechan.bloc_utils.event.EventInterface
import io.github.f77.simplechan.bloc_utils.event.SimpleSnackBarEventInterface
import io.github.f77.simplechan.bloc_utils.state.StateInterface
import io.github.f77.simplechan.events.main_activity.TitleUpdatedEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainActivityViewModel : BlocViewModel() {
    override fun mapEventToAction(event: EventInterface): Flow<ActionInterface> = flow {
        when (event) {
            is TitleUpdatedEvent -> {
                emit(TitleUpdatedAction(event.title))
            }
            is SimpleSnackBarEventInterface -> {
                emit(Actions.simpleSnackBar(event.message))
            }
        }
    }

    override fun mapEventToState(event: EventInterface): Flow<StateInterface> = flow {

    }
}
