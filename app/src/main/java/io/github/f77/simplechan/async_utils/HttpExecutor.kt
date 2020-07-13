package io.github.f77.simplechan.async_utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class HttpExecutor {
    companion object {
        fun <T> run(callback: () -> T): LiveData<Resource<T>> {
            val resourceData: MutableLiveData<Resource<T>> = MutableLiveData()

            Thread(Runnable {
                try {
                    resourceData.postValue(Resource.loading())
                    val callResult: T = callback()
                    resourceData.postValue(Resource.success(callResult))
                } catch (e: Throwable) {
                    resourceData.postValue(Resource.error(e))
                }
            }).start()

            return resourceData
        }
    }
}
