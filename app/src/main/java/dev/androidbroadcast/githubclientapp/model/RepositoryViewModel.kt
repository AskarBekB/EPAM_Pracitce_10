package dev.androidbroadcast.githubclientapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.androidbroadcast.githubclientapp.network.Repository
import dev.androidbroadcast.githubclientapp.network.RetrofitClient
import kotlinx.coroutines.launch

class RepositoryViewModel : ViewModel() {
    private val _repositories = MutableLiveData<List<Repository>>()
    val repositories: LiveData<List<Repository>> get() = _repositories

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadRepositories(org: String) {
        viewModelScope.launch {
            _isLoading.postValue(true) // Проверяем загрузку
            try {
                val response = RetrofitClient.api.getRepositories(org)
                if (response.isSuccessful) {
                    _repositories.postValue(response.body().orEmpty())
                    _error.postValue(null)  // Ошибок нет
                } else {
                    _error.postValue("Cannot find repositories")
                    _repositories.postValue(emptyList())
                }
            } catch (e: Exception) {
                _error.postValue("Cannot load repositories")
                _repositories.postValue(emptyList())
            } finally {
                _isLoading.postValue(false)  // Скрываем загрузку
            }
        }
    }
}
