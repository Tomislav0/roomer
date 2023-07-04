package com.tomislav0.roomer.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.tomislav0.roomer.dataAccess.AuthRepository
import com.tomislav0.roomer.dataAccess.AuthRepositoryImpl
import com.tomislav0.roomer.dataAccess.UserRepository
import com.tomislav0.roomer.dataAccess.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class AppModule {
    @Provides
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl(
        auth = Firebase.auth
    )

    @Provides
    fun provideUserRepository(): UserRepository = UserRepositoryImpl(
        auth = Firebase.auth,
        db = FirebaseFirestore.getInstance()
    )
}