package com.application.afol.vm.searchViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.afol.models.ApiResultSearch
import com.application.afol.network.Result
import com.application.afol.repositories.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SearchLegoViewModel(private val repository: Repository) : ViewModel() {
    private val job = Job()
    private val coroutineContext: CoroutineContext get() = job + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    val getSearchSuccess = MutableLiveData<ApiResultSearch>()
    val getSearchError = MutableLiveData<String>()
    val getSearchException = MutableLiveData<java.lang.Exception>()

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

    fun getLegoSetBySearch(searchQuery: String, page: Int, pageSize: Int) {
        scope.launch {
            when (val searchRespond = repository.getLegoBySearch(searchQuery, page, pageSize)) {
                is Result.Success -> getSearchSuccess.postValue(searchRespond.data)
                is Result.Error -> getSearchError.postValue(searchRespond.error)
                is Result.Exception -> getSearchException.postValue(searchRespond.exception)
            }
        }
    }

    private fun cancelJob() = job.cancel()
}