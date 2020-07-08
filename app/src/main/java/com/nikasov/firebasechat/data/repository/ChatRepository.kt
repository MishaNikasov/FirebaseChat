package com.nikasov.firebasechat.data.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nikasov.firebasechat.common.Const
import com.nikasov.firebasechat.data.chat.Message
import javax.inject.Inject

class ChatRepository @Inject constructor() {

    private val chatCollection = Firebase.firestore.collection(Const.MESSAGE_COLLECTION)

    fun addMessage(message: Message) {
        chatCollection.add(message)
    }

}