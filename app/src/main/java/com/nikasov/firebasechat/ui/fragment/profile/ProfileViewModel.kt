package com.nikasov.firebasechat.ui.fragment.profile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileViewModel @ViewModelInject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel(){


    val profile : MutableLiveData<FirebaseUser> = MutableLiveData()

    fun updateProfile() {

    }

    fun getProfileInfo() {
        profile.postValue(firebaseAuth.currentUser)
    }

    fun logOut() {
        firebaseAuth.signOut()
    }

}