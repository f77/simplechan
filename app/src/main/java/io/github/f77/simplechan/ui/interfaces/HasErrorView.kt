package io.github.f77.simplechan.ui.interfaces

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import io.github.f77.simplechan.R

interface HasErrorView {
    var errorLayout: ConstraintLayout
    var errorTextTextView: TextView
    var errorCodeTextView: TextView

    fun initErrorViews(rootView: View) {
        errorTextTextView = rootView.findViewById<TextView>(R.id.errorText).apply {
            visibility = View.GONE
        }
        errorCodeTextView = rootView.findViewById<TextView>(R.id.errorCode).apply {
            visibility = View.GONE
        }
        errorLayout = rootView.findViewById<ConstraintLayout>(R.id.errorLayout).apply {
            visibility = View.GONE
        }
    }

    fun setError(text: String?, code: Int? = null) {
        errorLayout.visibility = View.VISIBLE
        errorTextTextView.visibility = View.VISIBLE
        errorCodeTextView.visibility = View.GONE

        errorTextTextView.text = text ?: "Unknown Error"

        code?.let {
            errorCodeTextView.text = code.toString()
            errorCodeTextView.visibility = View.VISIBLE
        }
    }
}
