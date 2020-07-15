package io.github.f77.simplechan.ui.threads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.github.f77.simplechan.R

class ThreadsFragment : Fragment() {

    private lateinit var threadsViewModel: ThreadsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        threadsViewModel =
            ViewModelProviders.of(this).get(ThreadsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_threads, container, false)
        val textView: TextView = root.findViewById(R.id.text_threads)
        threadsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
