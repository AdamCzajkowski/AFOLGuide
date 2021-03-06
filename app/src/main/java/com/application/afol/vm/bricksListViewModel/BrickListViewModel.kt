package com.application.afol.vm.bricksListViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.afol.models.BrickResult
import com.application.afol.network.Result
import com.application.afol.repositories.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class BrickListViewModel(val repository: Repository) : ViewModel() {
    private val job = Job()
    private val coroutineContext: CoroutineContext get() = job + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    private val _isList = MutableLiveData<Boolean>()

    val isList: LiveData<Boolean>
        get() = _isList

    val getBricksSuccess = MutableLiveData<BrickResult>()
    val getBricksError = MutableLiveData<String>()
    val getBricksException = MutableLiveData<Exception>()

    init {
        _isList.value = true
    }

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

    fun toggleIsList() {
        _isList.value = isList.value != true
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