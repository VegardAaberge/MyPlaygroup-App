package com.myplaygroup.app.feature_profile.presentation.create_profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.R
import com.myplaygroup.app.core.presentation.app_bar.AppBarBackButton
import com.myplaygroup.app.core.presentation.camera.CameraScreen
import com.myplaygroup.app.core.presentation.components.CustomProgressIndicator
import com.myplaygroup.app.core.presentation.components.DefaultTopAppBar
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.core.presentation.components.scaffoldColumnModifier
import com.myplaygroup.app.feature_profile.presentation.create_profile.components.ProfileField
import com.myplaygroup.app.feature_profile.presentation.create_profile.components.ProfileImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun CreateProfileScreen(
    navigator: DestinationsNavigator? = null,
    viewModel: CreateProfileViewModel = hiltViewModel()
) {
    val takePictureMode = viewModel.state.takePictureMode
    val scaffoldState = collectEventFlow(viewModel, navigator)

    if(takePictureMode){
        CameraScreen(
            shouldCrop = true,
            scaffoldState = scaffoldState
        ){
            viewModel.onEvent(
                CreateProfileScreenEvent.TakePictureDone(it)
            )
        }
    }else{
        CreateProfileScreenBody(
            navigator = navigator,
            scaffoldState = scaffoldState,
            viewModel = viewModel
        )
    }
}

@Composable
fun CreateProfileScreenBody(
    navigator: DestinationsNavigator?,
    scaffoldState: ScaffoldState,
    viewModel: CreateProfileViewModel
) {

    val focusManager = LocalFocusManager.current
    val isBusy = viewModel.isBusy
    val state = viewModel.state

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.create_profile_title),
                navigationIcon = {
                    AppBarBackButton(navigator!!)
                }
            ){
                IconButton(
                    onClick = {
                        viewModel.onEvent(CreateProfileScreenEvent.SaveProfile)
                    },
                    enabled = !isBusy,
                ) {
                    Text(text = stringResource(R.string.create_profile_save))
                }
            }
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = scaffoldColumnModifier {
                focusManager.clearFocus()
            }
        ) {
            ProfileImage(
                profileBitmap = state.profileBitmap,
                takePicture = {
                    if(!isBusy){
                        focusManager.clearFocus()
                        viewModel.onEvent(CreateProfileScreenEvent.TakePicture)
                    }
                },
                profileBitmapError = state.profileBitmapError
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextFields(viewModel)
        }

        if(isBusy){
            CustomProgressIndicator()
        }
    }
}

@Composable
fun ColumnScope.TextFields(
    viewModel: CreateProfileViewModel
) {
    val isBusy = viewModel.isBusy
    val state = viewModel.state

    val widthModifier = Modifier
        .width(400.dp)
        .padding(horizontal = 30.dp)

    ProfileField(
        value = state.profileName,
        enabled = !isBusy,
        placeholder = stringResource(id = R.string.create_profile_name_placeholder),
        label = stringResource(id = R.string.create_profile_name_label),
        onTextChange = {
            viewModel.onEvent(CreateProfileScreenEvent.EnteredProfileName(it))
        },
        errorMessage = state.profileNameError,
        modifier = widthModifier,
    )

    ProfileField(
        value = state.phoneNumber,
        enabled = !isBusy,
        placeholder = stringResource(id = R.string.create_profile_phone_number_placeholder),
        label = stringResource(id = R.string.create_profile_screen_phone_number_label),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        onTextChange = {
            viewModel.onEvent(CreateProfileScreenEvent.EnteredPhoneNumber(it))
        },
        modifier = widthModifier,
        errorMessage = state.phoneNumberError
    )

    ProfileField(
        value = state.password,
        enabled = !isBusy,
        placeholder = stringResource(id = R.string.login_create_profile_password_placeholder),
        label = stringResource(id = R.string.login_create_profile_password_label),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isPassword = true,
        onTextChange = {
            viewModel.onEvent(CreateProfileScreenEvent.EnteredPassword(it))
        },
        modifier = widthModifier,
        errorMessage = state.passwordError
    )

    ProfileField(
        value = state.repeatedPassword,
        enabled = !isBusy,
        placeholder = stringResource(id = R.string.create_profile_confirm_password_placeholder),
        label = stringResource(id = R.string.create_profile_confirm_password_label),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isPassword = true,
        onTextChange = {
            viewModel.onEvent(CreateProfileScreenEvent.EnteredRepeatedPassword(it))
        },
        modifier = widthModifier,
        errorMessage = state.repeatedPasswordError
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CreateProfileScreen()
}