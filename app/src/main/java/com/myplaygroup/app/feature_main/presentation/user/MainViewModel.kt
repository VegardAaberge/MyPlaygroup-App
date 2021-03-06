package com.myplaygroup.app.feature_main.presentation.user


import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private var userSettingsManager: UserSettingsManager,
    private var imageRepository: ImageRepository
) : BaseViewModel(){

    val username = userSettingsManager.getFlow {
        it.map { u -> u.username }
    }

    val balance = MutableStateFlow(0L)

    var state by mutableStateOf(MainViewState())

    init {
        viewModelScope.launch {
            val username = username.first()
            state = state.copy(
                receiver = if(username == "admin") "vegard" else "admin"
            )

            loadProfileImage(username){
                state = state.copy(
                    usernameUri = it.data!!
                )
            }

            loadProfileImage(state.receiver, true){
                state = state.copy(
                    receiverUri = it.data!!
                )
            }
        }
    }

    private fun loadProfileImage(
        user: String,
        fetchFromRemote: Boolean = false,
        setState: (Resource<Uri?>) -> Unit
    ) = viewModelScope.launch(Dispatchers.IO) {

        val result = imageRepository.getProfileImage(
            user = user,
            fetchFromRemote = fetchFromRemote
        )

        if (result is Resource.Success) {
            launch(Dispatchers.Main) {
                setState(result)
            }
        } else if (result is Resource.Error) {
            setUIEvent(
                UiEvent.ShowSnackbar(result.message!!)
            )
        }
    }
}