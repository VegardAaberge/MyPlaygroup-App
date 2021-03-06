package com.myplaygroup.app.core.di

import com.myplaygroup.app.core.data.repository.ImageRepositoryImpl
import com.myplaygroup.app.core.data.repository.TokenRepositoryImpl
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.domain.repository.TokenRepository
import com.myplaygroup.app.feature_login.data.repository.LoginRepositoryImpl
import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import com.myplaygroup.app.feature_main.data.repository.*
import com.myplaygroup.app.feature_main.domain.repository.*
import com.myplaygroup.app.feature_profile.data.repository.ProfileRepositoryImpl
import com.myplaygroup.app.feature_profile.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLoginRepository(
        loginRepository: LoginRepositoryImpl
    ): LoginRepository

    @Binds
    @Singleton
    abstract fun bindImageRepository(
        imageRepository: ImageRepositoryImpl
    ): ImageRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        mainRepository: ChatRepositoryImpl
    ): ChatRepository

    @Binds
    @Singleton
    abstract fun bindChatSocketRepository(
        ChatSocketRepository: ChatSocketRepositoryImpl
    ): ChatSocketRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        profileRepository: ProfileRepositoryImpl
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindTokenRepository(
        tokenRepository: TokenRepositoryImpl
    ): TokenRepository

    @Binds
    @Singleton
    abstract fun bindDailyClassesRepository(
        dailyClassesRepository: DailyClassesRepositoryImpl
    ): DailyClassesRepository

    @Binds
    @Singleton
    abstract fun bindUsersRepository(
        usersRepository: UsersRepositoryImpl
    ): UsersRepository

    @Binds
    @Singleton
    abstract fun bindMonthlyPlansRepository(
        monthlyPlansRepository: MonthlyPlansRepositoryImpl
    ): MonthlyPlansRepository

    @Binds
    @Singleton
    abstract fun bindScheduleRepository(
        dailyClassesRepository: ScheduleRepositoryImpl
    ): ScheduleRepository

    @Binds
    @Singleton
    abstract fun bindPaymentRepository(
        paymentRepository: PaymentRepositoryImpl
    ): PaymentRepository
}