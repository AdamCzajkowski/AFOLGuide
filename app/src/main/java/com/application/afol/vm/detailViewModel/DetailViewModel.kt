package com.application.afol.vm.detailViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.afol.models.LegoSet
import com.application.afol.models.MOCResult
import com.application.afol.network.Result
import com.application.afol.repositories.Repository
import kotlinx.coroutines.*
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

    fun addToMySets(legoSet: LegoSet) = scope.launch {
        repository.addToMySets(legoSet)
    }

    suspend fun getListOfMySets(): LiveData<MutableList<LegoSet>> {
        return withContext(Dispatchers.Main) {
            return@withContext repository.getMySets()
        }
    }
}