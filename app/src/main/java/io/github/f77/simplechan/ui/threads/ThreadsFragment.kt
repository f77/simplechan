package io.github.f77.simplechan.ui.threads

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.bumptech.glide.ListPreloader.PreloadSizeProvider
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.FixedPreloadSizeProvider
import io.github.f77.simplechan.R
import io.github.f77.simplechan.actions.threads.ThreadInformationSelectedAction
import io.github.f77.simplechan.bloc_utils.BlocFragment
import io.github.f77.simplechan.bloc_utils.EventsAwareInterface
import io.github.f77.simplechan.bloc_utils.action.interfaces.ActionInterface
import io.github.f77.simplechan.bloc_utils.state.ErrorStateInterface
import io.github.f77.simplechan.bloc_utils.state.LoadingStateInterface
import io.github.f77.simplechan.bloc_utils.state.StateInterface
import io.github.f77.simplechan.events.threads.ThreadClosedEvent
import io.github.f77.simplechan.states.threads.ThreadsSuccessfulState
import io.github.f77.simplechan.swipes_decoration_utils.ItemSwipeTouchCallback
import io.github.f77.simplechan.ui.interfaces.HasErrorView
import io.github.f77.simplechan.ui.interfaces.HasGlideRequestManager
import io.github.f77.simplechan.ui.interfaces.HasProgressBarView
import io.github.f77.simplechan.ui.swipe_configs.DeleteSwipeConfig
import io.github.f77.simplechan.ui.swipe_configs.EditSwipeConfig

class ThreadsFragment : BlocFragment(), HasGlideRequestManager, HasErrorView, HasProgressBarView {
    // Models.
    override val viewModel: ThreadsViewModel by activityViewModels()

    // Views.
    override lateinit var progressBarView: ProgressBar
    override lateinit var errorLayout: ConstraintLayout
    override lateinit var errorTextTextView: TextView
    override lateinit var errorCodeTextView: TextView
    private lateinit var threadsRecyclerView: RecyclerView

    override lateinit var glideRequestManager: RequestManager
    private lateinit var threadsAdapter: ThreadsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_threads, container, false)
    }

    override fun onDestroyView() {
        viewModel.addEvent(ThreadClosedEvent())
        super.onDestroyView()
    }

    override fun initViews(rootView: View) {
        glideRequestManager = Glide.with(this)

        initErrorViews(rootView)
        initProgressBarViews(rootView)

        threadsRecyclerView = rootView.findViewById<RecyclerView>(R.id.recycler_view_threads).apply {
            configureRecyclerView(this)
            visibility = View.GONE
        }
    }

    override fun handleAction(action: ActionInterface) {
        when (action) {
            is ThreadInformationSelectedAction -> {
                //println("THREAD INFORMATION CLICKED ON " + threadsAdapter.dataset[it.position])
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
            is ThreadsSuccessfulState -> {
                renderSuccess(state)
            }
        }
    }

    private fun renderLoading(state: LoadingStateInterface) {
        TransitionManager.beginDelayedTransition(rootViewGroup)
        progressBarView.visibility = View.VISIBLE
        errorLayout.visibility = View.GONE
        threadsRecyclerView.visibility = View.GONE
    }

    private fun renderError(state: ErrorStateInterface) {
        state.exception.printStackTrace()

        TransitionManager.beginDelayedTransition(rootViewGroup)
        progressBarView.visibility = View.GONE
        threadsRecyclerView.visibility = View.GONE
        setError(state.exception.message)
    }

    private fun renderSuccess(state: ThreadsSuccessfulState) {
        // Directly copy link to the dataset from the model.
        threadsAdapter.dataset = state.threads
        threadsAdapter.notifyDataSetChanged()

        // @TODO: Be careful with this animation.
        TransitionManager.beginDelayedTransition(rootViewGroup)

        progressBarView.visibility = View.GONE
        errorLayout.visibility = View.GONE
        threadsRecyclerView.visibility = View.VISIBLE
    }

    private fun configureRecyclerView(recyclerView: RecyclerView) {
        viewManager = LinearLayoutManager(requireContext())
        threadsAdapter = ThreadsAdapter(viewModel, glideRequestManager)

        // Configure Glide's integration with RecyclerView.
        val sizeProvider: PreloadSizeProvider<String> =
            FixedPreloadSizeProvider<String>(ThreadsAdapter.GLIDE_WIDTH, ThreadsAdapter.GLIDE_HEIGHT)
        val preloader: RecyclerViewPreloader<String> =
            RecyclerViewPreloader(glideRequestManager, threadsAdapter, sizeProvider, 10)

        recyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = threadsAdapter

            // Configure caching
            setItemViewCacheSize(10)

            // Add control of the swipes and move.
            val callback = makeItemSwipeTouchCallback(requireContext(), viewModel)
            ItemTouchHelper(callback).attachToRecyclerView(this)

            // Add Glide integration
            addOnScrollListener(preloader)
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
