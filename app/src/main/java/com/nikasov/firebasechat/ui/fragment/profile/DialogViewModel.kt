package com.nikasov.firebasechat.ui.fragment.profile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.nikasov.firebasechat.data.chat.Dialog
import com.nikasov.firebasechat.data.repository.DialogRepository
import com.nikasov.firebasechat.data.repository.ProfileRepository
import com.nikasov.firebasechat.data.user.Profile
import com.nikasov.firebasechat.util.Resource
import kotlinx.coroutines.launch

class DialogViewModel @ViewModelInject constructor(
    private val profileRepository: ProfileRepository,
    private val dialogRepository: DialogRepository,
    private val auth: FirebaseAuth
) : ViewModel(){

    val profile : MutableLiveData<Resource<Profile>> = MutableLiveData()
    val dialogs : MutableLiveData<Resource<List<Dialog>>> = MutableLiveData()

    private lateinit var currentProfileId : String

    fun getProfile(profileId : String) {
        currentProfileId = profileId
        viewModelScope.launch {
            profile.postValue(
                Resource.Success(
                    profileRepository.getProfile(profileId)
                )
            )
        }
    }

    fun addDialog() {
        viewModelScope.launch {
            dialogRepository.addDialog(
                currentProfileId
            )
        }
    }

    fun getDialogs() {
        viewModelScope.launch {
            dialogs.postValue(Resource.Success(
                dialogRepository.getDialogs(currentProfileId))
            )
        }
    }

    fun logOut() {
        auth.signOut()
    }
}