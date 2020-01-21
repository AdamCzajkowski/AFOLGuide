package com.application.afol.vm.detailViewModel

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
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

    suspend fun getListOfFavorites(): LiveData<MutableList<LegoSet>> {
        return withContext(Dispatchers.IO) {
            return@withContext repository.getFavorites()
        }
    }

    fun removeFromFavorites(legoSet: LegoSet) = scope.launch {
        legoSet.isInFavorite = false
        repository.removeFromFavorites(legoSet)
    }

    fun addToFavorites(legoSet: LegoSet) = scope.launch {
        legoSet.isInFavorite = true
        repository.addToFavorites(legoSet)
    }

    private fun cancelJob() = job.cancel()
}
