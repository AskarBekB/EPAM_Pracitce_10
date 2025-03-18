package dev.androidbroadcast.githubclientapp.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.androidbroadcast.githubclientapp.R
import dev.androidbroadcast.githubclientapp.network.RepositoryDetails
import dev.androidbroadcast.githubclientapp.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RepositoryDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val _repositoryDetails = MutableLiveData<RepositoryDetails>()
    val repositoryDetails: LiveData<RepositoryDetails> get() = _repositoryDetails

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadRepositoryDetails(owner: String, repo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                val response = RetrofitClient.api.getRepositoryDetails(owner, repo)
                if (response.isSuccessful) {
                    _repositoryDetails.postValue(response.body())
                    _error.postValue(null)
                } else {
                    _error.postValue(getApplication<Application>().getString(R.string.cannot_load_details))
                }
            } catch (e: Exception) {
                _error.postValue(getApplication<Application>().getString(R.string.cannot_load_repository_details))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
