package com.nikasov.firebasechat.ui.data

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun firebaseSignInWithGoogle(googleAuthCredential: AuthCredential){
        firebaseAuth.signInWithCredential(googleAuthCredential).await()
    }

}