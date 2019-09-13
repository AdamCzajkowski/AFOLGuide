package com.application.afol.vm.themesViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.afol.models.ThemesResult
import com.application.afol.network.Result
import com.application.afol.repositories.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ThemesViewModel(private val repository: Repository) : ViewModel() {
    private val job = Job()

    private val coroutineContext: CoroutineContext get() = job + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    val getThemesSuccess = MutableLiveData<ThemesResult>()
    val getThemesError = MutableLiveData<String>()
    val getThemesException = MutableLiveData<Exception>()

    fun getThemes(page: Int, pageSize: Int) {
        scope.launch {
            when (val themesRespond = repository.getThemes(page, pageSize)) {
                is Result.Success<ThemesResult> -> getThemesSuccess.postValue(themesRespond.data)
                is Result.Error -> getThemesError.postValue(themesRespond.error)
                is Result.Exception -> getThemesException.postValue(themesRespond.exception)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

    private fun cancelJob() = job.cancel()
}