package com.nikasov.firebasechat.ui.fragment.chat

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.nikasov.firebasechat.common.Const
import com.nikasov.firebasechat.data.chat.Message
import com.nikasov.firebasechat.data.repository.ChatRepository
import com.nikasov.firebasechat.util.Resource

class ChatViewModel @ViewModelInject constructor(
    private val auth: FirebaseAuth,
    private val chatRepository: ChatRepository
): ViewModel() {

    private val chatCollection = Firebase.firestore.collection(Const.CHAT_COLLECTION)

    var messages : MutableLiveData<Resource<List<Message>>> = MutableLiveData()

    fun addMessage(messageText: String) {
        chatRepository.addMessage(
            Message(
                auth.uid!!,
                messageText
            )
        )
    }

    fun subscribeToMessages() {
        val messagesArray = arrayListOf<Message>()
        chatCollection.addSnapshotListener { query, exception ->
            exception?.let {
                messages.postValue(Resource.Error(exception.localizedMessage))
            }
            query?.let {
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

}