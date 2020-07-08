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

    var messages : MutableLiveData<Resource<List<Message>>> = MutableLiveData()

    lateinit var profileId : String
    lateinit var dialogId : String

    fun setCollection (profileId : String, dialogId : String) {
        chatRepository.setCollection(profileId, dialogId)
        this.profileId = profileId
        this.dialogId = dialogId
    }

    fun addMessage(messageText: String) {
        chatRepository.addMessage(
            Message(
                profileId,
                messageText
            )
        )
    }

    fun subscribeToMessages() {
        val messageCollection =
            Firebase.firestore.collection(Const.PROFILE_COLLECTION)
            .document(profileId).collection(Const.DIALOG_COLLECTION)
            .document(dialogId).collection(Const.DIALOG_COLLECTION)

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
}