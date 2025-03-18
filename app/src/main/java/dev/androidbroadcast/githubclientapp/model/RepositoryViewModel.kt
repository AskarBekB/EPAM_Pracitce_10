package dev.androidbroadcast.githubclientapp.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.androidbroadcast.githubclientapp.R
import dev.androidbroadcast.githubclientapp.network.Repository
import dev.androidbroadcast.githubclientapp.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RepositoryViewModel(application: Application) : AndroidViewModel(application) {
    private val _repositories = MutableLiveData<List<Repository>>()
    val repositories: LiveData<List<Repository>> get() = _repositories

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadRepositories(org: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                val response = RetrofitClient.api.getRepositories(org)
                if (response.isSuccessful) {
                    _repositories.postValue(response.body().orEmpty())
                    _error.postValue(null)
                } else {
                    _error.postValue(getApplication<Application>().getString(R.string.cannot_find_repositories))
                    _repositories.postValue(emptyList())
                }
            } catch (e: Exception) {
                _error.postValue(getApplication<Application>().getString(R.string.cannot_load_repositories))
                _repositories.postValue(emptyList())
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}

