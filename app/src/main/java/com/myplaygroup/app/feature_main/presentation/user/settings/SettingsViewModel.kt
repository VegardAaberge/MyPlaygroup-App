package com.myplaygroup.app.feature_main.presentation.user.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.destinations.*
import com.myplaygroup.app.feature_main.domain.interactors.MainDaoInteractor
import com.myplaygroup.app.feature_main.presentation.user.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val basicAuthInterceptor: BasicAuthInterceptor,
    private val imageRepository: ImageRepository,
    private val userSettingsManager: UserSettingsManager,
    private val daoUseCases: MainDaoInteractor
) : ViewModel() {

    lateinit var mainViewModel: MainViewModel

    val profileName: Flow<String>
    val phoneNumber: Flow<String>

    init {
        profileName = userSettingsManager.getFlow { it.map { u -> u.profileName } }
        phoneNumber = userSettingsManager.getFlow { it.map { u -> u.phoneNumber } }
    }

    fun onEvent(event: SettingsScreenEvent){
        when(event){
            is SettingsScreenEvent.LogoutButtonTapped -> {
                logout()
            }
            is SettingsScreenEvent.NavigateToShowBalance -> {
                mainViewModel.setUIEvent(
                    BaseViewModel.UiEvent.NavigateTo(BalanceScreenDestination)
                )
            }
            is SettingsScreenEvent.EditProfileTapped -> {
                mainViewModel.setUIEvent(
                    BaseViewModel.UiEvent.NavigateTo(EditProfileScreenDestination(event.editProfileType.name))
                )
            }
            is SettingsScreenEvent.EditProfilePictureTapped -> {
                mainViewModel.setUIEvent(
                    BaseViewModel.UiEvent.NavigateTo(ProfileSelectorScreenDestination)
                )
            }
        }
    }

    private fun logout() = viewModelScope.launch {
        basicAuthInterceptor.accessToken = null
        userSettingsManager.clearData()
        daoUseCases.clearAllTables()
        imageRepository.clearProfileImages()

        mainViewModel.setUIEvent(
            BaseViewModel.UiEvent.PopAndNavigateTo(
                popRoute = MainScreenDestination.route,
                destination = LoginScreenDestination
            )
        )
    }
}