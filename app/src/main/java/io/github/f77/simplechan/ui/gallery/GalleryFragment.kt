package io.github.f77.simplechan.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.github.f77.simplechan.R
import io.github.f77.simplechan.bloc_utils.state.ErrorStateInterface
import io.github.f77.simplechan.bloc_utils.state.LoadingStateInterface
import io.github.f77.simplechan.entities.BoardEntity
import io.github.f77.simplechan.states.BoardsSuccessState

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)


        viewManager = LinearLayoutManager(requireActivity().applicationContext)

        val data: MutableList<BoardEntity> = mutableListOf()

        // !!! Set empty adapter first.
        viewAdapter = MyAdapter(data)

        recyclerView = root.findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }

        galleryViewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                is LoadingStateInterface -> {
                    println("BOARDS LOADING STATE")
                }
                is ErrorStateInterface -> {
                    println("BOARDS ERROR STATE")
                    it.exception.printStackTrace()
                    Snackbar.make(root, it.exception::class.java.name + "!", Snackbar.LENGTH_LONG).show()
                }
                is BoardsSuccessState -> {
                    println("BOARDS DATA SUCCESSFULLY RECEIVED!")
                    data.clear()
                    data.addAll(it.boards)
                    viewAdapter.notifyDataSetChanged()
                }
            }
        })

        return root
    }
}
