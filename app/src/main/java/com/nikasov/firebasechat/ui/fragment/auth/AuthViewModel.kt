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

    var currentUser : MutableLiveData<Resource<String>> = MutableLiveData()

    fun setCurrentUser(resource: Resource.Success<String>? = null) {
        if (resource != null) {
            currentUser.postValue(resource)
        } else {
            if (auth.currentUser != null) {
                currentUser.postValue(Resource.Success(auth.currentUser!!.uid))
            }
        }
    }

    fun signInWithGoogle (googleAuthCredential : AuthCredential) {
        currentUser.postValue(Resource.Loading())
        viewModelScope.launch {
            setUser(authRepository.firebaseLogInWithGoogle(googleAuthCredential))
        }
    }

    fun signUpWithEmail(name: String, email : String, password : String) {
        currentUser.postValue(Resource.Loading())
        viewModelScope.launch {
            setUser(authRepository.signUpWithEmail(name, email, password))
        }
    }

    fun logInWithEmail(email : String, password : String) {
        currentUser.postValue(Resource.Loading())
        viewModelScope.launch {
            setUser(authRepository.logInWithEmail(email, password))
        }
    }

    private fun setUser(user : AuthResource<Profile>) {
        when (user) {
            is AuthResource.SignUp -> {
                user.data?.let { profile ->
                    viewModelScope.launch {
                        profileRepository.saveProfile(profile)
                        setCurrentUser(Resource.Success(profile.uid!!))
                    }
                }
            }
            is AuthResource.LogIn -> {
                user.id?.let {id ->
                    setCurrentUser(Resource.Success(id))
                }
            }
            is AuthResource.Error -> {
                user.message?.let { message ->
                    currentUser.postValue(Resource.Error(message))
                }
            }
        }
    }
}