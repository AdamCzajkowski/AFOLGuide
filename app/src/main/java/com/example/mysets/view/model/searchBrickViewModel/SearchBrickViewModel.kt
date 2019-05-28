package com.example.mysets.view.model.searchBrickViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysets.models.PartResult
import com.example.mysets.repositories.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class SearchBrickViewModel(val repository: Repository) : ViewModel() {
    private val job = Job()
    private val coroutineContext: CoroutineContext get() = job + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    val getPartsSuccess = MutableLiveData<PartResult>()
    val getPartsError = MutableLiveData<String>()
    val getPartsException = MutableLiveData<Exception>()

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

    fun getBricksFromSearch(page: Int, pageSize: Int, searchQuery: String) {
        scope.launch {
            when (val partResponse = repository.getPartBySearch(page, pageSize, searchQuery)) {
                is com.example.mysets.network.Result.Success -> getPartsSuccess.postValue(
                    partResponse.data as PartResult
                )
                is com.example.mysets.network.Result.Error -> getPartsError.postValue(partResponse.error)
                is com.example.mysets.network.Result.Exception -> getPartsException.postValue(
                    partResponse.exception
                )
            }
        }
    }

    private fun cancelJob() = job.cancel()
}