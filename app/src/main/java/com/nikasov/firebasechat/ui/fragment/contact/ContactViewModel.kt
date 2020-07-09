package com.nikasov.firebasechat.ui.fragment.contact

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikasov.firebasechat.data.repository.DialogRepository
import com.nikasov.firebasechat.data.repository.ProfileRepository
import com.nikasov.firebasechat.data.user.Profile
import com.nikasov.firebasechat.util.Prefs
import com.nikasov.firebasechat.util.Resource
import kotlinx.coroutines.launch

class ContactViewModel @ViewModelInject constructor(
    private val dialogRepository: DialogRepository,
    private val profileRepository: ProfileRepository
): ViewModel() {

    val contacts : MutableLiveData<List<Profile>> = MutableLiveData()

    fun getAllContacts() {
        viewModelScope.launch {
            contacts.postValue(profileRepository.getAllProfiles())
        }
    }
}