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
class AppModule {
    @Singleton
    @Provides
    fun provideFirebaseAuth() : FirebaseAuth = FirebaseAuth.getInstance()
    @Provides
    @Singleton
    fun provideResourceProvider (@ApplicationContext context: Context) = ResourceProvider(context)
    @Provides
    @Singleton
    fun provideSignInOption(@ApplicationContext context: Context, resourceProvider : ResourceProvider) : GoogleSignInClient {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resourceProvider.getString(R.string.default_token))
            .requestEmail()
            .requestProfile()
            .build()
        return GoogleSignIn.getClient(context, options)
    }
}