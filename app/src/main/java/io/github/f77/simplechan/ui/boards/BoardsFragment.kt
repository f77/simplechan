package io.github.f77.simplechan.ui.boards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.github.f77.simplechan.R
import io.github.f77.simplechan.bloc_utils.state.ErrorStateInterface
import io.github.f77.simplechan.bloc_utils.state.LoadingStateInterface
import io.github.f77.simplechan.states.BoardsSuccessState
import io.github.f77.simplechan.swipes_decoration_utils.ItemSwipeTouchCallback
import io.github.f77.simplechan.swipes_decoration_utils.ItemSwipesDefaultObserver

class BoardsFragment : Fragment() {
    private lateinit var boardsViewModel: BoardsViewModel
    private lateinit var boardsRecyclerView: RecyclerView
    private lateinit var boardsAdapter: BoardsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        boardsViewModel = ViewModelProviders.of(this).get(BoardsViewModel::class.java)

        // Find views
        val root = inflater.inflate(R.layout.fragment_boards, container, false)
        val progressBar = root.findViewById<ProgressBar>(R.id.progressBar)

        viewManager = LinearLayoutManager(requireContext())
        boardsAdapter = BoardsAdapter()

        boardsRecyclerView = root.findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = boardsAdapter

            // Add control of the swipes and move.
            val touchCallback = decorateItemSwipeTouchCallback(ItemSwipeTouchCallback(boardsViewModel))
            ItemTouchHelper(touchCallback).attachToRecyclerView(this)
        }

        // Observe actions.
        boardsViewModel.actions.observe(viewLifecycleOwner, Observer {
            ItemSwipesDefaultObserver.notifyItemsChanges(it, boardsAdapter)
        })

        // Observe UI states.
        boardsViewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                is LoadingStateInterface -> {
                    println("BOARDS LOADING STATE")
                    progressBar.visibility = View.VISIBLE
                }
                is ErrorStateInterface -> {
                    println("BOARDS ERROR STATE")
                    progressBar.visibility = View.INVISIBLE
                    it.exception.printStackTrace()
                    Snackbar.make(root, it.exception::class.java.name + "!", Snackbar.LENGTH_LONG).show()
                }
                is BoardsSuccessState -> {
                    println("BOARDS DATA SUCCESSFULLY RECEIVED!")
                    progressBar.visibility = View.INVISIBLE

                    // Directly copy link to the dataset from the model.
                    boardsAdapter.dataset = it.boards
                    boardsAdapter.notifyDataSetChanged()
                }
            }
        })

        return root
    }

    private fun decorateItemSwipeTouchCallback(callback: ItemSwipeTouchCallback): ItemSwipeTouchCallback {
        return callback.apply {
            isItemViewSwipe = true
            isLongPressDrag = true
            leftLabel = "DELETE"
            rightLabel = "EDIT"
            leftLabelColor = ContextCompat.getColor(requireContext(), R.color.colorSwipeColor)
            rightLabelColor = ContextCompat.getColor(requireContext(), R.color.colorSwipeColor)
            leftBackgroundColor = ContextCompat.getColor(requireContext(), R.color.colorSwipeDeleteBackground)
            rightBackgroundColor = ContextCompat.getColor(requireContext(), R.color.colorSwipeEditBackground)
            leftIcon = android.R.drawable.ic_menu_delete
            rightIcon = android.R.drawable.ic_menu_edit
        }
    }
}
