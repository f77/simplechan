package io.github.f77.simplechan.bloc_utils.action

interface ItemMovedActionInterface : ActionInterface {
    val fromPosition: Int
    val toPosition: Int
}
