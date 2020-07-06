package com.nikasov.firebasechat.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.nikasov.firebasechat.data.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
class DataModule {
    @Provides
    fun provideAuthRepo(@ApplicationContext context: Context, firebaseAuth: FirebaseAuth)
            : AuthRepository =
        AuthRepository(
            context,
            firebaseAuth
        )

}