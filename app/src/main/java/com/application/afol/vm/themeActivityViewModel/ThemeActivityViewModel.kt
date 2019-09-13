package com.application.afol.vm.themeActivityViewModel

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

class ThemeActivityViewModel(private val repository: Repository) : ViewModel() {
    private val job = Job()

    private val coroutineContext: CoroutineContext get() = job + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    val getThemesSuccess = MutableLiveData<ApiResultSearch>()
    val getThemesError = MutableLiveData<String>()
    val getThemesException = MutableLiveData<Exception>()

    fun getThemes(page: Int, pageSize: Int, themeId: Int) {
        scope.launch {
            when (val themesRespond = repository.getLegoFromTheme(page, pageSize, themeId)) {
                is Result.Success -> getThemesSuccess.postValue(themesRespond.data)
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