package com.nikasov.firebasechat.ui.fragment.dialog

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.nikasov.firebasechat.data.chat.Dialog
import com.nikasov.firebasechat.data.repository.DialogRepository
import com.nikasov.firebasechat.data.repository.ProfileRepository
import com.nikasov.firebasechat.data.user.Profile
import com.nikasov.firebasechat.util.Prefs
import com.nikasov.firebasechat.util.Resource
import kotlinx.coroutines.launch

class DialogViewModel @ViewModelInject constructor(
    private val dialogRepository: DialogRepository,
    prefs: Prefs
) : ViewModel(){

    val profile : MutableLiveData<Resource<Profile>> = MutableLiveData()
    val dialogs : MutableLiveData<Resource<List<Dialog>>> = MutableLiveData()

    private val currentProfileId = prefs.loadProfileId()

    fun getDialogs() {
        viewModelScope.launch {
            dialogs.postValue(Resource.Success(
                dialogRepository.getDialogs(currentProfileId!!))
            )
        }
    }

}