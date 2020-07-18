package io.github.f77.simplechan.actions.threads

import io.github.f77.simplechan.bloc_utils.action.interfaces.ActionInterface
import io.github.f77.simplechan.bloc_utils.interfaces.HasPositionInterface

class ThreadInformationSelectedAction(override val position: Int) : ActionInterface, HasPositionInterface {
}
