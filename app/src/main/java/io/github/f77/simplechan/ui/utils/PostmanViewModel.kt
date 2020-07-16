package io.github.f77.simplechan.ui.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * This view model is used to pass data between fragments.
 */
abstract class PostmanViewModel : ViewModel() {
    val data = MutableLiveData<Any>()

    fun putData(item: Any) {
        data.value = item
    }
}
