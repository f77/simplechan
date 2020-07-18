package io.github.f77.simplechan.ui.boards

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.github.f77.simplechan.R
import io.github.f77.simplechan.bloc_utils.BlocFragment
import io.github.f77.simplechan.bloc_utils.action.interfaces.ActionInterface
import io.github.f77.simplechan.bloc_utils.action.interfaces.NavigateActionInterface
import io.github.f77.simplechan.bloc_utils.state.ErrorStateInterface
import io.github.f77.simplechan.bloc_utils.state.LoadingStateInterface
import io.github.f77.simplechan.bloc_utils.state.StateInterface
import io.github.f77.simplechan.states.boards.BoardsSuccessState
import io.github.f77.simplechan.swipes_decoration_utils.ItemSwipeTouchCallback
import io.github.f77.simplechan.swipes_decoration_utils.ItemSwipesDefaultObserver
import io.github.f77.simplechan.ui.swipe_configs.DeleteSwipeConfig
import io.github.f77.simplechan.ui.swipe_configs.EditSwipeConfig


class BoardsFragment : BlocFragment() {
    override val viewModel: BoardsViewModel by activityViewModels()
    private val selectedBoardModel: SelectedBoardViewModel by activityViewModels()

    private lateinit var progressBarView: ProgressBar
    private lateinit var boardsRecyclerView: RecyclerView
    private lateinit var boardsAdapter: BoardsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewManager = LinearLayoutManager(requireContext())
        return inflater.inflate(R.layout.fragment_boards, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init views and vars
        progressBarView = view.findViewById<ProgressBar>(R.id.progressBar)
        boardsRecyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_boards)
        boardsAdapter = BoardsAdapter(viewModel)

        // Configure RecyclerView
        boardsRecyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = boardsAdapter

            // Add control of the swipes and move.
            val touchCallback = decorateItemSwipeTouchCallback(
                requireContext(),
                ItemSwipeTouchCallback(viewModel)
            )
            ItemTouchHelper(touchCallback).attachToRecyclerView(this)

            // Add line separators.
            boardsRecyclerView.addItemDecoration(
                DividerItemDecoration(
                    boardsRecyclerView.context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }


    override fun observeActions(): Observer<ActionInterface> = Observer {
        // Handle RecyclerView swipes and moves.
        ItemSwipesDefaultObserver.notifyItemsChanges(it, boardsAdapter)

        when (it) {
            is NavigateActionInterface -> {
                // Pass data to new fragment.
                selectedBoardModel.putData(boardsAdapter.dataset[it.fromPosition])

                // Open threads fragment.
                val action = BoardsFragmentDirections.actionNavBoardsToNavThreads()
                boardsRecyclerView.findViewHolderForAdapterPosition(it.fromPosition)?.itemView
                    ?.findNavController()?.navigate(action)
            }
        }
    }

    override fun observeStates(): Observer<StateInterface> = Observer {
        when (it) {
            is LoadingStateInterface -> {
                println("BOARDS LOADING STATE")
                progressBarView.visibility = View.VISIBLE
            }
            is ErrorStateInterface -> {
                println("BOARDS ERROR STATE")
                progressBarView.visibility = View.INVISIBLE
                it.exception.printStackTrace()
                Snackbar.make(rootView, it.exception.message.toString(), Snackbar.LENGTH_LONG).show()
            }
            is BoardsSuccessState -> {
                println("BOARDS DATA SUCCESSFULLY RECEIVED!")
                progressBarView.visibility = View.INVISIBLE

                // Directly copy link to the dataset from the model.
                boardsAdapter.dataset = it.boards
                boardsAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun decorateItemSwipeTouchCallback(
        context: Context,
        callback: ItemSwipeTouchCallback
    ): ItemSwipeTouchCallback {
        return callback.apply {
            isItemViewSwipe = true
            isLongPressDrag = false
            leftSwipeConfig = DeleteSwipeConfig(context)
            rightSwipeConfig = EditSwipeConfig(context)
        }
    }
}
