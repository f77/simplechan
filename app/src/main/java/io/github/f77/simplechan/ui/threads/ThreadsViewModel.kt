package io.github.f77.simplechan.ui.threads

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ThreadsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is threads Fragment"
    }
    val text: LiveData<String> = _text
}
