package io.github.f77.simplechan.bloc_utils

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import io.github.f77.simplechan.bloc_utils.action.interfaces.ActionInterface
import io.github.f77.simplechan.bloc_utils.state.StateInterface

abstract class BlocFragment : Fragment() {
    protected abstract val viewModel: BlocViewModelInterface

    protected lateinit var rootView: View

    abstract fun observeActions(): Observer<ActionInterface>
    abstract fun observeStates(): Observer<StateInterface>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        viewModel.actions.observe(viewLifecycleOwner, observeActions())
        viewModel.state.observe(viewLifecycleOwner, observeStates())
    }
}
