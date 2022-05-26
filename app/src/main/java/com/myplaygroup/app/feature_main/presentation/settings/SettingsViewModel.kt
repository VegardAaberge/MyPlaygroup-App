package com.myplaygroup.app.feature_main.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.domain.Settings.UserSettingsManager
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.destinations.EditProfileScreenDestination
import com.myplaygroup.app.destinations.LoginScreenDestination
import com.myplaygroup.app.destinations.MainScreenDestination
import com.myplaygroup.app.destinations.ProfileSelectorScreenDestination
import com.myplaygroup.app.feature_main.domain.repository.MainRepository
import com.myplaygroup.app.feature_main.presentation.MainViewModel
import com.myplaygroup.app.feature_main.presentation.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val basicAuthInterceptor: BasicAuthInterceptor,
    private val imageRepository: ImageRepository,
    private val repository: MainRepository,
    private val userSettingsManager: UserSettingsManager
) : ViewModel() {

    lateinit var mainViewModel: MainViewModel

    val username: Flow<String>
    val profileName: Flow<String>

    var state by mutableStateOf(HomeState())

    init {
        username = userSettingsManager.getFlow { it.map { u -> u.username } }
        profileName = userSettingsManager.getFlow { it.map { u -> u.profileName } }

        viewModelScope.launch {
            val uri = imageRepository.getProfileImage()
            state = state.copy(
                imageUri = uri.data
            )
        }
    }

    fun onEvent(event: SettingsScreenEvent){
        when(event){
            is SettingsScreenEvent.LogoutButtonTapped -> {
                logout()
            }
            is SettingsScreenEvent.EditProfileTapped -> {
                mainViewModel.setUIEvent(
                    BaseViewModel.UiEvent.NavigateTo(EditProfileScreenDestination)
                )
            }
            is SettingsScreenEvent.EditProfilePictureTapped -> {
                mainViewModel.setUIEvent(
                    BaseViewModel.UiEvent.NavigateTo(ProfileSelectorScreenDestination)
                )
            }
        }
    }

    fun logout() = viewModelScope.launch {
        basicAuthInterceptor.accessToken = null
        userSettingsManager.clearData()
        repository.clearAllTables()

        mainViewModel.setUIEvent(
            BaseViewModel.UiEvent.PopAndNavigateTo(
                popRoute = MainScreenDestination.route,
                destination = LoginScreenDestination
            )
        )
    }
}