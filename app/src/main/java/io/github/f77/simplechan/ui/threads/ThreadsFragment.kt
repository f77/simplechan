package io.github.f77.simplechan.ui.threads

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.github.f77.simplechan.R
import io.github.f77.simplechan.actions.threads.ThreadInformationSelectedAction
import io.github.f77.simplechan.bloc_utils.BlocFragment
import io.github.f77.simplechan.bloc_utils.action.interfaces.ActionInterface
import io.github.f77.simplechan.bloc_utils.state.ErrorStateInterface
import io.github.f77.simplechan.bloc_utils.state.LoadingStateInterface
import io.github.f77.simplechan.bloc_utils.state.StateInterface
import io.github.f77.simplechan.entities.BoardEntity
import io.github.f77.simplechan.events.threads.ThreadsBoardGivenEvent
import io.github.f77.simplechan.states.threads.ThreadsSuccessfulState
import io.github.f77.simplechan.swipes_decoration_utils.ItemSwipeTouchCallback
import io.github.f77.simplechan.ui.boards.SelectedBoardViewModel
import io.github.f77.simplechan.ui.swipe_configs.DeleteSwipeConfig
import io.github.f77.simplechan.ui.swipe_configs.EditSwipeConfig

class ThreadsFragment : BlocFragment() {
    override val viewModel: ThreadsViewModel by activityViewModels()
    private val selectedBoardModel: SelectedBoardViewModel by activityViewModels()

    private lateinit var progressBarView: ProgressBar
    private lateinit var threadsRecyclerView: RecyclerView
    private lateinit var threadsAdapter: ThreadsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewManager = LinearLayoutManager(requireContext())
        return inflater.inflate(R.layout.fragment_threads, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the board from previous fragment.
        selectedBoardModel.data.observe(viewLifecycleOwner, Observer {
            it as BoardEntity
            viewModel.addEvent(ThreadsBoardGivenEvent(it))
        })

        // Init views and vars
        progressBarView = view.findViewById<ProgressBar>(R.id.progressBar)
        threadsRecyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_threads)
        threadsAdapter = ThreadsAdapter(viewModel)

        // Configure RecyclerView
        threadsRecyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = threadsAdapter

            // Configure caching
            threadsRecyclerView.setItemViewCacheSize(10)

            // Add control of the swipes and move.
            val touchCallback = decorateItemSwipeTouchCallback(
                requireContext(),
                ItemSwipeTouchCallback(viewModel)
            )
            ItemTouchHelper(touchCallback).attachToRecyclerView(this)
        }
    }

    override fun observeActions(): Observer<ActionInterface> = Observer {
        when (it) {
            is ThreadInformationSelectedAction -> {
                //println("THREAD INFORMATION CLICKED ON " + threadsAdapter.dataset[it.position])
            }
        }
    }

    override fun observeStates(): Observer<StateInterface> = Observer {
        when (it) {
            is LoadingStateInterface -> {
                println("THREADS LOADING STATE")
                progressBarView.visibility = View.VISIBLE
            }
            is ErrorStateInterface -> {
                println("THREADS ERROR STATE")
                progressBarView.visibility = View.INVISIBLE
                it.exception.printStackTrace()
                Snackbar.make(rootView, it.exception.message.toString(), Snackbar.LENGTH_LONG).show()
            }
            is ThreadsSuccessfulState -> {
                println("THREADS DATA SUCCESSFULLY RECEIVED!")
                progressBarView.visibility = View.INVISIBLE

                // Directly copy link to the dataset from the model.
                threadsAdapter.dataset = it.threads
                threadsAdapter.notifyDataSetChanged()
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
