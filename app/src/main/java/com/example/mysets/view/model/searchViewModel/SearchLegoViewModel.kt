package com.example.mysets.view.model.searchViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysets.models.LegoSet
import com.example.mysets.network.Result
import com.example.mysets.repositories.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SearchLegoViewModel(private val repository: Repository) : ViewModel() {
    private val job = Job()
    private val coroutineContext: CoroutineContext get() = job + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    private val getLegoSetSuccess = MutableLiveData<LegoSet>()
    private val getLegoSetError = MutableLiveData<String>()
    private val getLegoSetException = MutableLiveData<Exception>()

    init {
        startFetchLegoSet()
    }

    fun startFetchLegoSet() {
        startFetchSampleLegoSet()
    }

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

    private fun startFetchSampleLegoSet() {
        scope.launch {
            when(val legoRespond = repository.getSampleLegoSet()) {
                is Result.Success -> getLegoSetSuccess.postValue(legoRespond.data)
                is Result.Error -> getLegoSetError.postValue(legoRespond.error)
                is Result.Exception -> getLegoSetException.postValue(legoRespond.exception)
            }
        }
    }

    fun getLegoError(): LiveData<Result<List<String?>>> {
        val resultError = MediatorLiveData<Result<List<String?>>>()
        resultError.addSource(getLegoSetError) {
            resultError.value = combineErrorLegoData(getLegoSetError)
        }
        return resultError
    }

    fun getLegoException(): LiveData<Result<List<Exception?>>> {
        val resultException = MediatorLiveData<Result<List<Exception?>>>()
        resultException.addSource(getLegoSetException) {
            resultException.value = combineExceptionLegoData(getLegoSetException)
        }
        return resultException
    }

    fun getLegoSuccess(): LiveData<Result<List<LegoSet?>>> {
        val resultSuccess = MediatorLiveData<Result<List<LegoSet?>>>()
        resultSuccess.addSource(getLegoSetSuccess) {
            resultSuccess.value = combineSuccessLegoData(getLegoSetSuccess)
        }
        return resultSuccess
    }

    private fun combineExceptionLegoData(
        _LegoSet: MutableLiveData<Exception>
    ): Result<List<Exception?>> {
        val legoSet: Exception? = _LegoSet.value
        return Result.Respond(
            listOf<Exception?>(
                legoSet
            )
        )
    }

    private fun combineSuccessLegoData(
        _LegoSet: MutableLiveData<LegoSet>
    ): Result<List<LegoSet?>> {
        val legoSet: LegoSet? = _LegoSet.value
        return Result.Respond(
            listOf<LegoSet?>(
                legoSet
            )
        )
    }

    private fun combineErrorLegoData(
        _LegoSet: MutableLiveData<String>
    ): Result<List<String?>> {
        val legoSet: String? = _LegoSet.value
        return Result.Respond(
            listOf<String?>(
                legoSet
            )
        )
    }

    private fun cancelJob() = job.cancel()
}