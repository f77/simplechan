package io.github.f77.simplechan.ui.boards

import android.content.Context
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.f77.simplechan.R
import io.github.f77.simplechan.bloc_utils.BlocFragment
import io.github.f77.simplechan.bloc_utils.EventsAwareInterface
import io.github.f77.simplechan.bloc_utils.action.interfaces.ActionInterface
import io.github.f77.simplechan.bloc_utils.action.interfaces.NavigateActionInterface
import io.github.f77.simplechan.bloc_utils.state.ErrorStateInterface
import io.github.f77.simplechan.bloc_utils.state.LoadingStateInterface
import io.github.f77.simplechan.bloc_utils.state.StateInterface
import io.github.f77.simplechan.events.threads.ThreadsBoardGivenEvent
import io.github.f77.simplechan.states.boards.BoardsSuccessState
import io.github.f77.simplechan.swipes_decoration_utils.ItemSwipeTouchCallback
import io.github.f77.simplechan.swipes_decoration_utils.ItemSwipesDefaultObserver
import io.github.f77.simplechan.ui.interfaces.HasErrorView
import io.github.f77.simplechan.ui.swipe_configs.DeleteSwipeConfig
import io.github.f77.simplechan.ui.swipe_configs.EditSwipeConfig
import io.github.f77.simplechan.ui.threads.ThreadsViewModel

class BoardsFragment : BlocFragment(), HasErrorView {
    override val viewModel: BoardsViewModel by activityViewModels()
    private val threadsViewModel: ThreadsViewModel by activityViewModels()

    private lateinit var boardsAdapter: BoardsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    // Views.
    private lateinit var progressBarView: ProgressBar
    override lateinit var errorLayout: ConstraintLayout
    override lateinit var errorTextTextView: TextView
    override lateinit var errorCodeTextView: TextView

    private lateinit var boardsRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_boards, container, false)
    }

    override fun initViews(rootView: View) {
        initErrorViews(rootView)
        progressBarView = rootView.findViewById<ProgressBar>(R.id.progressBar).apply {
            visibility = View.GONE
        }

        boardsRecyclerView = rootView.findViewById<RecyclerView>(R.id.recycler_view_boards).apply {
            initRecyclerView(this)
            visibility = View.GONE
        }
    }

    override fun handleAction(action: ActionInterface) {
        // Handle RecyclerView swipes and moves.
        ItemSwipesDefaultObserver.notifyItemsChanges(action, boardsAdapter)

        when (action) {
            is NavigateActionInterface -> {
                // Pass data to the new fragment.
                val boardEntity = boardsAdapter.dataset[action.fromPosition]
                threadsViewModel.addEvent(ThreadsBoardGivenEvent(boardEntity))

                // Open threads fragment.
                val direction = BoardsFragmentDirections.actionNavBoardsToNavThreads()
                findNavController().navigate(direction)
            }
        }
    }

    override fun render(state: StateInterface) {
        when (state) {
            is LoadingStateInterface -> {
                renderLoading(state)
            }
            is ErrorStateInterface -> {
                renderError(state)
            }
            is BoardsSuccessState -> {
                renderSuccess(state)
            }
        }
    }

    private fun renderLoading(state: LoadingStateInterface) {
        println("BOARDS LOADING STATE")

        TransitionManager.beginDelayedTransition(rootViewGroup)
        progressBarView.visibility = View.VISIBLE
        errorLayout.visibility = View.GONE
        boardsRecyclerView.visibility = View.GONE
    }

    private fun renderError(state: ErrorStateInterface) {
        println("BOARDS ERROR STATE")
        state.exception.printStackTrace()

        TransitionManager.beginDelayedTransition(rootViewGroup)
        progressBarView.visibility = View.GONE
        boardsRecyclerView.visibility = View.GONE
        setError(state.exception.message)
    }

    private fun renderSuccess(state: BoardsSuccessState) {
        // Directly copy link to the dataset from the model.
        boardsAdapter.dataset = state.boards
        boardsAdapter.notifyDataSetChanged()

        TransitionManager.beginDelayedTransition(rootViewGroup)
        progressBarView.visibility = View.GONE
        errorLayout.visibility = View.GONE
        boardsRecyclerView.visibility = View.VISIBLE
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        viewManager = LinearLayoutManager(requireContext())
        boardsAdapter = BoardsAdapter(viewModel)

        recyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = boardsAdapter

            // Add control of the swipes and move.
            val callback = makeItemSwipeTouchCallback(requireContext(), viewModel)
            ItemTouchHelper(callback).attachToRecyclerView(this)

            // Add line separators.
            addItemDecoration(
                DividerItemDecoration(
                    recyclerView.context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun makeItemSwipeTouchCallback(context: Context, viewModel: EventsAwareInterface): ItemSwipeTouchCallback {
        return ItemSwipeTouchCallback(viewModel).apply {
            isItemViewSwipe = true
            isLongPressDrag = false
            leftSwipeConfig = EditSwipeConfig(context)
            rightSwipeConfig = DeleteSwipeConfig(context)
        }
    }
}
