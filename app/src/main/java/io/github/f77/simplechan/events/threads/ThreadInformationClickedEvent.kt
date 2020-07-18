package io.github.f77.simplechan.events.threads

import io.github.f77.simplechan.bloc_utils.interfaces.HasPositionInterface

class ThreadInformationClickedEvent(override val position: Int) : ThreadsEvent(), HasPositionInterface {
}
