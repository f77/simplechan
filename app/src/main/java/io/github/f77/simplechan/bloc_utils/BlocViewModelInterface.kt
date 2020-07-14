package io.github.f77.simplechan.bloc_utils

import androidx.lifecycle.MutableLiveData

interface BlocViewModelInterface<in EventType, StateType> {
    /**
     * State is a current condition of the UI.
     */
    val state: MutableLiveData<StateType>

    /**
     * Actions are like events, but directed from viewModel to UI.
     * For example, navigation events, SnackBar, dialog, etc.
     */
    val actions: MutableLiveData<StateType>

    /**
     * Add event to the ViewModel.
     */
    fun addEvent(event: EventType)
}
