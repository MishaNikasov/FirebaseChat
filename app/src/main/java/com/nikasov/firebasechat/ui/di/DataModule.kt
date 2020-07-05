package com.nikasov.firebasechat.ui.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.nikasov.firebasechat.R
import com.nikasov.firebasechat.common.ResourceProvider
import com.nikasov.firebasechat.ui.data.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DataModule {
    @Provides
    fun provideAuthRepo(@ApplicationContext context: Context, firebaseAuth: FirebaseAuth)
            : AuthRepository = AuthRepository(context, firebaseAuth)
}