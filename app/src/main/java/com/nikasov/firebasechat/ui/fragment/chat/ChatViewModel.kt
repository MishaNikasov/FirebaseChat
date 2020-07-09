package com.nikasov.firebasechat.ui.fragment.chat

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.nikasov.firebasechat.common.Const
import com.nikasov.firebasechat.data.chat.Message
import com.nikasov.firebasechat.data.repository.ChatRepository
import com.nikasov.firebasechat.data.repository.DialogRepository
import com.nikasov.firebasechat.data.repository.ProfileRepository
import com.nikasov.firebasechat.util.Prefs
import com.nikasov.firebasechat.util.Resource
import kotlinx.coroutines.launch

class ChatViewModel @ViewModelInject constructor(
    private val chatRepository: ChatRepository,
    private val profileRepository: ProfileRepository,
    private val dialogRepository: DialogRepository,
    private val prefs: Prefs
): ViewModel() {

    var messages : MutableLiveData<Resource<List<Message>>> = MutableLiveData()
    var loading : MutableLiveData<Resource<String?>> = MutableLiveData()

    private val profileId = getCurrentId()

    private lateinit var messageCollection : CollectionReference

    fun checkIfDialogExist(memberId : String) {
        addNewDialog(memberId)
    }

    fun addNewDialog(memberId : String) {
        loading.postValue(Resource.Loading())
        viewModelScope.launch {
            val profile = profileRepository.getProfile(memberId)
            dialogRepository.addDialog(
                profileId,
                memberId,
                profile.name
            ).let { res ->
                when (res) {
                    is Resource.Success -> {
                        loading.postValue(Resource.Empty())
                        setExistingDialog(res.data!!)
                    }
                    is Resource.Error -> {
                        loading.postValue(Resource.Error(res.message))
                    }
                }
            }
        }
    }

    fun setExistingDialog(dialogId : String) {
        messageCollection =
            Firebase.firestore
                .collection(Const.DIALOG_COLLECTION)
                .document(dialogId)
                .collection(Const.MESSAGE_COLLECTION)
        subscribeToMessages()
    }

    fun addMessage(messageText: String) {
        messageCollection.add(
            Message(
                messageText,
                profileId
            )
        )
    }

    private fun subscribeToMessages() {
        val messagesArray = arrayListOf<Message>()
        messageCollection.orderBy("date").addSnapshotListener { query, exception ->
            exception?.let {
                messages.postValue(Resource.Error(exception.localizedMessage))
            }
            query?.let {
                messagesArray.clear()
                for (messageObject in query.documents) {
                    val message = messageObject.toObject<Message>()
                    message?.let {
                        messagesArray.add(message)
                    }
                }
                messages.postValue(Resource.Success(messagesArray))
            }
        }
    }

    fun getCurrentId(): String {
        return prefs.loadProfileId() as String
    }
}