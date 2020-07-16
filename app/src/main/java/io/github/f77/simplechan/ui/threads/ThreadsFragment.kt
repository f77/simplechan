package io.github.f77.simplechan.ui.threads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import io.github.f77.simplechan.R
import io.github.f77.simplechan.bloc_utils.BlocFragment
import io.github.f77.simplechan.bloc_utils.action.interfaces.ActionInterface
import io.github.f77.simplechan.bloc_utils.state.StateInterface
import io.github.f77.simplechan.entities.BoardEntity
import io.github.f77.simplechan.events.threads.ThreadsBoardGivenEvent
import io.github.f77.simplechan.states.threads.ThreadsSuccessfulState
import io.github.f77.simplechan.ui.boards.SelectedBoardViewModel

class ThreadsFragment : BlocFragment() {
    override val viewModel: ThreadsViewModel by activityViewModels()
    private val selectedBoardModel: SelectedBoardViewModel by activityViewModels()

    lateinit var textDataTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_threads, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init views and vars
        textDataTextView = view.findViewById(R.id.text_threads)

        // Get the board from previous fragment.
        selectedBoardModel.data.observe(viewLifecycleOwner, Observer {
            viewModel.addEvent(ThreadsBoardGivenEvent(it as BoardEntity))
        })
    }

    override fun observeActions(): Observer<ActionInterface> = Observer {

    }

    override fun observeStates(): Observer<StateInterface> = Observer {
        when (it) {
            is ThreadsSuccessfulState -> {
                textDataTextView.text = it.board.id
            }
        }
    }
}
