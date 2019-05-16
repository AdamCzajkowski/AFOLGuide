package com.example.mysets.view.model.bricksListViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysets.models.BrickResult
import com.example.mysets.network.Result
import com.example.mysets.repositories.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class BrickListViewModel(val repository: Repository) : ViewModel() {
    private val job = Job()
    private val coroutineContext: CoroutineContext get() = job + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    val getBricksSuccess = MutableLiveData<BrickResult>()
    val getBricksError = MutableLiveData<String>()
    val getBricksException = MutableLiveData<Exception>()

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

    fun getBricksFromSet(page: Int, setNumber: String, pageSize: Int) {
        scope.launch {
            when (val bricksResponse = repository.getBricksFromSet(page, setNumber, pageSize)) {
                is Result.Success -> getBricksSuccess.postValue(bricksResponse.data)
                is Result.Error -> getBricksError.postValue(bricksResponse.error)
                is Result.Exception -> getBricksException.postValue(bricksResponse.exception)
            }
        }
    }

    private fun cancelJob() = job.cancel()
}