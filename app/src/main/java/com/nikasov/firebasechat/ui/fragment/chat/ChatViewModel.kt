package com.nikasov.firebasechat.ui.fragment.chat

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.nikasov.firebasechat.common.Const
import com.nikasov.firebasechat.data.chat.Message
import com.nikasov.firebasechat.data.repository.ChatRepository
import com.nikasov.firebasechat.util.Prefs
import com.nikasov.firebasechat.util.Resource
import kotlinx.coroutines.launch

class ChatViewModel @ViewModelInject constructor(
    private val chatRepository: ChatRepository,
    private val prefs: Prefs
): ViewModel() {

    var messages : MutableLiveData<Resource<List<Message>>> = MutableLiveData()

    lateinit var profileId : String
    lateinit var dialogId : String

    fun setCollection (dialogId : String) {
        chatRepository.setCollection(dialogId)
        this.profileId = getCurrentId()
        this.dialogId = dialogId
    }

    fun addMessage(messageText: String) {
        chatRepository.addMessage(
            Message(
                messageText,
                profileId
            )
        )
    }//todo: открывать чистой страницой диалог а только потом создавать если еще не согздано

    fun subscribeToMessages() {
        val messageCollection =
            Firebase.firestore.collection(Const.DIALOG_COLLECTION)
            .document(dialogId).collection(Const.MESSAGE_COLLECTION)

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