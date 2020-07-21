package io.github.f77.simplechan.ui.interfaces

import android.view.View
import android.widget.ProgressBar
import io.github.f77.simplechan.R

interface HasProgressBarView {
    var progressBarView: ProgressBar

    fun initProgressBarViews(rootView: View) {
        progressBarView = rootView.findViewById<ProgressBar>(R.id.progressBar).apply {
            visibility = View.GONE
        }
    }
}
