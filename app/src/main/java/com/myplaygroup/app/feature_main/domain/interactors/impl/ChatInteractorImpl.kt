package com.myplaygroup.app.feature_main.domain.interactors.impl

import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.data.local.MainDao
import com.myplaygroup.app.feature_main.domain.interactors.ChatInteractor
import com.myplaygroup.app.feature_main.domain.model.AppUser
import com.myplaygroup.app.feature_main.domain.model.ChatGroup
import com.myplaygroup.app.feature_main.domain.model.Message
import com.myplaygroup.app.feature_main.domain.repository.ChatRepository
import com.myplaygroup.app.feature_main.domain.repository.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatInteractorImpl @Inject constructor (
    private val chatRepository: ChatRepository,
    private val usersRepository: UsersRepository,
    private val userSettingsManager: UserSettingsManager,
    private val dao: MainDao
) : ChatInteractor {

    override suspend fun getChatGroups(users: List<AppUser>, oldChatGroups: List<ChatGroup>): Flow<Resource<List<ChatGroup>>> {

        return flow {
            emit(Resource.Loading(true))

            val username = userSettingsManager.getFlow { it.map { u -> u.username }}.first()
            emit(
                getChatGroupFromMessages(
                    users = users,
                    username = username,
                    currentChatGroups = oldChatGroups
                )
            )

            chatRepository.getChatMessages(true, true).collect{ result ->
                when(result){
                    is Resource.Success -> {
                        emit(
                            getChatGroupFromMessages(
                                users = users,
                                username = username,
                                messages = result.data!!,
                                currentChatGroups = oldChatGroups
                            )
                        )
                    }
                    is Resource.Error -> {
                        emit(Resource.Error(result.message!!))
                    }
                    else -> {}
                }
            }

            emit(Resource.Loading(false))
        }
    }

    private suspend fun getChatGroupFromMessages(
        users: List<AppUser>,
        username: String,
        messages: List<Message> = emptyList(),
        currentChatGroups: List<ChatGroup>
    ): Resource<List<ChatGroup>> {

        val chatGroups = users
            .filter { x -> x.username != username }
            .filter { x -> x.profileCreated }
            .map { appUser ->

                val userMessages = messages.filter { message -> hasMessage(message, appUser, username) }
                val lastMessage = userMessages.maxByOrNull { x -> x.created }

                val currentIcon = currentChatGroups.firstOrNull {
                        chatGroup -> chatGroup.username == appUser.username
                }?.icon

                ChatGroup(
                    username = appUser.username,
                    lastMessage = lastMessage?.message,
                    updateTime = lastMessage?.created,
                    icon = currentIcon,
                    messages = userMessages,
                )
            }

        return Resource.Success(chatGroups)
    }

    private fun hasMessage(message: Message, appUser: AppUser, username: String) : Boolean {

        if(message.createdBy == appUser.username)
            return true

        if(message.createdBy == username && message.receivers.contains(appUser.username))
            return true

        return false
    }

    override suspend fun getChatMessages(user: String): Flow<Resource<List<Message>>> {
        TODO("Not yet implemented")
    }
}