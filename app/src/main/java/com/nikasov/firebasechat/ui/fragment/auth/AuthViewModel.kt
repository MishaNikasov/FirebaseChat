package com.nikasov.firebasechat.ui.fragment.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nikasov.firebasechat.data.repository.AuthRepository
import com.nikasov.firebasechat.data.repository.ProfileRepository
import com.nikasov.firebasechat.data.user.Profile
import com.nikasov.firebasechat.util.AuthResource
import com.nikasov.firebasechat.util.Resource
import kotlinx.coroutines.launch

class AuthViewModel @ViewModelInject constructor(
    private val auth : FirebaseAuth,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
): ViewModel(){

    var currentUser : MutableLiveData<Resource<FirebaseUser?>> = MutableLiveData()

    fun setCurrentUser() {
        currentUser.postValue(Resource.Success(auth.currentUser))
    }

    fun signInWithGoogle (googleAuthCredential : AuthCredential) {
        viewModelScope.launch {
            setUser(authRepository.firebaseLogInWithGoogle(googleAuthCredential))
        }
    }

    fun signUpWithEmail(name: String, email : String, password : String) {
        viewModelScope.launch {
            setUser(authRepository.signUpWithEmail(name, email, password))
        }
    }

    fun logInWithEmail(email : String, password : String) {
        viewModelScope.launch {
            setUser(authRepository.logInWithEmail(email, password))
        }
    }

    private fun setUser(user : AuthResource<Profile>) {
        currentUser.postValue(Resource.Loading())
        when (user) {
            is AuthResource.SignUp -> {
                user.data?.let {
                    viewModelScope.launch {
                        profileRepository.saveProfile(user.data)
                        setCurrentUser()
                    }
                }
            }
            is AuthResource.LogIn -> {
                setCurrentUser()
            }
            is AuthResource.Error -> {
                currentUser.postValue(Resource.Error(user.message!!))
            }
        }
    }
}