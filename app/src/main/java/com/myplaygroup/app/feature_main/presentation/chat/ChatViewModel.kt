package com.myplaygroup.app.feature_main.presentation.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.Message
import com.myplaygroup.app.feature_main.domain.repository.MainRepository
import com.myplaygroup.app.feature_main.presentation.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    lateinit var mainViewModel: MainViewModel
    var state by mutableStateOf(ChatState())

    init {
        getMessages(true)
    }

    private fun getMessages(
        fetchFromRemote: Boolean = false
    ){
        viewModelScope.launch(Dispatchers.IO) {
            repository
                .getChatMessages(fetchFromRemote)
                .collect { collectGetMessages(it)}
        }
    }

    private fun collectGetMessages(result: Resource<List<Message>>) = viewModelScope.launch(Dispatchers.Main) {
        when (result) {
            is Resource.Success -> {
                state = state.copy(
                    messages = result.data!!,
                    showProgressIndicator = state.isLoading && result.data!!.isEmpty()
                )
            }
            is Resource.Error -> {
                mainViewModel.setUIEvent(
                    BaseViewModel.UiEvent.ShowSnackbar(result.message!!)
                )
                state = state.copy(
                    messages = result.data!!,
                )
            }
            is Resource.Loading -> {
                state = state.copy(
                    isLoading = result.isLoading,
                    showProgressIndicator = result.isLoading
                )
            }
        }
    }
}