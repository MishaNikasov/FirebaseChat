package com.nikasov.firebasechat.ui.fragment.profile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.nikasov.firebasechat.data.repository.ProfileRepository
import com.nikasov.firebasechat.data.user.Profile
import com.nikasov.firebasechat.util.Resource
import kotlinx.coroutines.launch

class ProfileViewModel @ViewModelInject constructor(
    private val profileRepository: ProfileRepository,
    private val auth: FirebaseAuth
) : ViewModel(){

    val profile : MutableLiveData<Resource<Profile>> = MutableLiveData()

    fun getProfile() {
        viewModelScope.launch {
            profile.postValue(Resource.Success(profileRepository.getProfile()))
        }
    }

    fun logOut() {
        auth.signOut()
    }
}