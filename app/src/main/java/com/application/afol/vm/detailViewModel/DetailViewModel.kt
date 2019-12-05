package com.application.afol.vm.detailViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.afol.models.MOCResult
import com.application.afol.network.Result
import com.application.afol.repositories.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetailViewModel(private val repository: Repository) : ViewModel() {

    private val job = Job()
    private val coroutineContext: CoroutineContext get() = job + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    val getMocsSuccess = MutableLiveData<MOCResult>()
    val getMocsError = MutableLiveData<String>()
    val getMocsException = MutableLiveData<Exception>()

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

    fun getMOCs(setNumber: String) {
        scope.launch {
            when (val mocsRespond = repository.getLegoAlternatives(setNumber)) {
                is Result.Success -> getMocsSuccess.postValue(mocsRespond.data)
                is Result.Error -> getMocsError.postValue(mocsRespond.error)
                is Result.Exception -> getMocsException.postValue(mocsRespond.exception)
            }
        }
    }

    private fun cancelJob() = job.cancel()

}