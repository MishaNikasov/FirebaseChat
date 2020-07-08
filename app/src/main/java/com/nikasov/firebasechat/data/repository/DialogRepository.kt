package com.nikasov.firebasechat.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.nikasov.firebasechat.common.Const
import com.nikasov.firebasechat.data.chat.Dialog
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class DialogRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
){

    private val profileCollection = Firebase.firestore.collection(Const.PROFILE_COLLECTION)

    fun addDialog(owner: String) {
        try {
            val currentDialog =
                profileCollection
                    .document(owner)
                    .collection(Const.DIALOG_COLLECTION)

            currentDialog.add(Dialog(
                owner,
                "TEST"
            ))
        } catch (e: Exception) {

        }
    }

    suspend fun getDialogs(owner: String) : List<Dialog> {
        try {
            val currentDialog =
            profileCollection
                .document(owner)
                .collection(Const.DIALOG_COLLECTION)

            val dialogs : ArrayList<Dialog> = arrayListOf()

            val profileQuery = currentDialog
                .get()
                .await()

            profileQuery.forEach { dialog ->
                dialogs.add(dialog.toObject())
            }

            return dialogs

        } catch (e: Exception) {
            return emptyList()
        }
    }
}