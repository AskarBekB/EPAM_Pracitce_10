package dev.androidbroadcast.githubclientapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.androidbroadcast.githubclientapp.network.RepositoryDetails
import dev.androidbroadcast.githubclientapp.network.RetrofitClient
import kotlinx.coroutines.launch

class RepositoryDetailsViewModel : ViewModel() {
    private val _repositoryDetails = MutableLiveData<RepositoryDetails>()
    val repositoryDetails: LiveData<RepositoryDetails> get() = _repositoryDetails

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadRepositoryDetails(owner: String, repo: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)  // Показываем загрузку
            try {
                val response = RetrofitClient.api.getRepositoryDetails(owner, repo)
                if (response.isSuccessful) {
                    _repositoryDetails.postValue(response.body())
                    _error.postValue(null)
                } else {
                    _error.postValue("Cannot load details")
                }
            } catch (e: Exception) {
                _error.postValue("Cannot load repository details")
            } finally {
                _isLoading.postValue(false)  // Скрываем загрузку
            }
        }
    }
}
