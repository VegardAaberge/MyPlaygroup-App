package com.myplaygroup.app.feature_main.presentation.admin.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.myplaygroup.app.R
import com.myplaygroup.app.core.presentation.components.CustomProgressIndicator
import com.myplaygroup.app.core.presentation.components.SearchTextField
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_main.domain.enums.ParametersType
import com.myplaygroup.app.feature_main.presentation.admin.AdminScreenEvent
import com.myplaygroup.app.feature_main.presentation.admin.AdminState
import com.myplaygroup.app.feature_main.presentation.admin.AdminViewModel
import com.myplaygroup.app.feature_main.presentation.admin.users.components.ShowCreateUserDialog
import com.plcoding.stockmarketapp.presentation.company_listings.components.UserItem

@Composable
fun UsersScreen(
    adminViewModel: AdminViewModel,
    viewModel: UsersViewModel
) {
    CreateToolbarActionItems(
        viewModel = viewModel,
        adminViewModel = adminViewModel
    )

    CatchOnResume(viewModel)

    val scaffoldState = collectEventFlow(viewModel = viewModel)
    val state = viewModel.state

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            if(state.isSearching){
                SearchTextField(
                    searchValue = state.searchValue,
                    onSearchChanged = {
                        viewModel.onEvent(UsersScreenEvent.OnSearchChanged(it))
                    }
                )
            }

            UsersLazyColumn(
                adminViewModel = adminViewModel,
                state = state
            )
        }

        if(state.showCreateUser){
            Dialog(
                onDismissRequest = {
                    viewModel.onEvent(UsersScreenEvent.CreateUserDialog(false))
                },
                properties = DialogProperties()
            ) {
                ShowCreateUserDialog(
                    createErrorMessage = state.createErrorMessage,
                    createUser = {
                        viewModel.onEvent(UsersScreenEvent.CreateUser(it))
                    }
                )
            }
        }

        if(viewModel.isBusy || adminViewModel.isBusy){
            CustomProgressIndicator()
        }
    }
}

@Composable
private fun UsersLazyColumn(
    adminViewModel: AdminViewModel,
    state: UsersState
){
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ){
        items(state.appUsers.size){ i ->
            val appUser = state.appUsers[i]
            UserItem(
                appUser = appUser,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        adminViewModel.onEvent(
                            AdminScreenEvent.NavigateToEditScreen(
                                type = ParametersType.USERS,
                                clientId = appUser.clientId
                            )
                        )
                    }
                    .padding(16.dp)
            )
            if(i < state.appUsers.size){
                Divider(modifier = Modifier.padding(
                    horizontal = 16.dp
                ))
            }
        }
    }
}

@Composable
private fun CreateToolbarActionItems(
    viewModel: UsersViewModel,
    adminViewModel: AdminViewModel
){
    val icon = ImageVector.vectorResource(id = R.drawable.ic_baseline_cloud_upload_24)
    LaunchedEffect(key1 = viewModel.getUnsyncedUsers(), key2 = viewModel.state.searchValue.isBlank()){
        val actionButtons = mutableListOf<AdminState.ActionButton>()
        if(viewModel.state.searchValue.isBlank() && viewModel.getUnsyncedUsers().any()){
            actionButtons.add(
                AdminState.ActionButton(
                    action = {
                        viewModel.onEvent(UsersScreenEvent.UploadAppUsers)
                    },
                    icon = icon
                ),
            )
        }
        if(viewModel.state.searchValue.isBlank()){
            actionButtons.add(
                AdminState.ActionButton(
                    action = {
                        viewModel.onEvent(UsersScreenEvent.CreateUserDialog(true))
                    },
                    icon = Icons.Default.Add
                )
            )
        }
        actionButtons.add(
            AdminState.ActionButton(
                action = {
                    viewModel.onEvent(UsersScreenEvent.TriggerSearch)
                },
                icon = Icons.Default.Search
            )
        )

        adminViewModel.state = adminViewModel.state.copy(
            actionButtons = actionButtons
        )
    }
}

@Composable
fun CatchOnResume(
    viewModel: UsersViewModel
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if(event == Lifecycle.Event.ON_RESUME){
                viewModel.onEvent(UsersScreenEvent.RefreshData)
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}