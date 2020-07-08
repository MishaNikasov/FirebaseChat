package com.nikasov.firebasechat.data.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.nikasov.firebasechat.common.Const
import com.nikasov.firebasechat.data.chat.Dialog
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DialogRepository @Inject constructor(

){

    private val dialogCollection = Firebase.firestore.collection(Const.DIALOG_COLLECTION)

    suspend fun addDialog(owner: String) {
        dialogCollection.add(Dialog(
            owner,
            "TEST"
        ))
    }

    suspend fun getDialogs(owner: String) : List<Dialog>{
        val dialogs : ArrayList<Dialog> = arrayListOf()
        val profileQuery = dialogCollection
            .whereEqualTo("owner", owner)
            .get()
            .await()
        profileQuery.forEach { dialog ->
            dialogs.add(dialog.toObject())
        }
        return dialogs
    }
}