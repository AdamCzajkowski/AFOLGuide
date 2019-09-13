package com.application.afol.vm.searchBrickViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.afol.models.PartResult
import com.application.afol.repositories.Repository
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
                is com.application.afol.network.Result.Success -> getPartsSuccess.postValue(
                    partResponse.data as PartResult
                )
                is com.application.afol.network.Result.Error -> getPartsError.postValue(partResponse.error)
                is com.application.afol.network.Result.Exception -> getPartsException.postValue(
                    partResponse.exception
                )
            }
        }
    }

    private fun cancelJob() = job.cancel()
}