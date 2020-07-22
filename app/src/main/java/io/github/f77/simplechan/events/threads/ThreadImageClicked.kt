package io.github.f77.simplechan.events.threads

import io.github.f77.simplechan.bloc_utils.interfaces.HasPositionInterface

class ThreadImageClicked(override val position: Int) : ThreadsEvent(), HasPositionInterface {
}
