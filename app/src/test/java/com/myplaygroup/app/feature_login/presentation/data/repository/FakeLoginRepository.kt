package com.myplaygroup.app.feature_login.presentation.data.repository

import com.myplaygroup.app.core.data.model.AppUser
import com.myplaygroup.app.core.domain.settings.UserRole
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_login.data.responses.LoginResponse
import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLoginRepository : LoginRepository {

    companion object{
        const val ADMIN = "ADMIN"
        const val HAS_PROFILE = "HAS_PROFILE"
        const val NO_PROFILE = "NO_PROFILE"
        const val PASSWORD = "b11111111B"
        const val INCORRECT_USERNAME_PASSWORD = "Incorrect username or password"
    }

    private var users = mutableListOf<AppUser>(
        AppUser(
            username = ADMIN,
            password = PASSWORD,
            appUserRole = UserRole.ADMIN,
            profileCreated = true,
            profileName = "Playgroup",
            phoneNumber = "12345678901"
        ),
        AppUser(
            username = HAS_PROFILE,
            password = PASSWORD,
            appUserRole = UserRole.USER,
            profileCreated = true,
            profileName = "Vegard",
            phoneNumber = "12345678901"
        ),
        AppUser(
            username = NO_PROFILE,
            password = PASSWORD,
            appUserRole = UserRole.USER,
            profileCreated = false
        )
    )

    override suspend fun authenticate(
        username: String,
        password: String
    ): Flow<Resource<LoginResponse>> {
        return flow {
            val currentUser = users.firstOrNull { x -> x.username == username }
            if(currentUser == null){
                emit(Resource.Error(INCORRECT_USERNAME_PASSWORD));
                return@flow
            }

            if (currentUser.password != password) {
                emit(Resource.Error(INCORRECT_USERNAME_PASSWORD));
            } else {
                emit(
                    Resource.Success(
                        LoginResponse(
                            access_token = "access_token",
                            refresh_token = "refresh_token",
                            user_role = currentUser.appUserRole.name,
                            profile_created = currentUser.profileCreated,
                            profile_name = currentUser.profileName,
                            phone_number = currentUser.phoneNumber
                        )
                    )
                )
            }
        }
    }
}