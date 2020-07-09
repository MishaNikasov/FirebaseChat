package com.nikasov.firebasechat.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nikasov.firebasechat.common.Const
import com.nikasov.firebasechat.data.chat.Message
import javax.inject.Inject

class ChatRepository @Inject constructor() {

    private lateinit var messageCollection : CollectionReference

    fun setCollection(dialogId : String) {
        messageCollection =
            Firebase.firestore
            .collection(Const.DIALOG_COLLECTION)
            .document(dialogId).collection(Const.MESSAGE_COLLECTION)
    }

    fun addMessage(message: Message) {
        messageCollection.add(message)
    }

}