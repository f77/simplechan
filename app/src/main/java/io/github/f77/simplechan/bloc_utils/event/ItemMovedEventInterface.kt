package io.github.f77.simplechan.bloc_utils.event

interface ItemMovedEventInterface : EventInterface {
    val fromPosition: Int
    val toPosition: Int
}
