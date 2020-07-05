package com.nikasov.firebasechat.ui.activity

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nikasov.firebasechat.ui.data.AuthRepository
import com.nikasov.firebasechat.ui.util.Resource
import kotlinx.coroutines.launch

class AuthViewModel @ViewModelInject constructor(
    private val auth : FirebaseAuth,
    private val authRepository: AuthRepository
): ViewModel(){

    var currentUser : MutableLiveData<Resource<FirebaseUser?>> = MutableLiveData()

    fun setCurrentUser() {
        currentUser.postValue(Resource.Success(auth.currentUser))
    }

    fun signInWithGoogle (googleAuthCredential : AuthCredential) {
        viewModelScope.launch {
            currentUser.postValue(Resource.Loading())
            authRepository.firebaseSignInWithGoogle(googleAuthCredential)
            setCurrentUser()
        }
    }
}