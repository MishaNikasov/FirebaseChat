package com.nikasov.firebasechat.data.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nikasov.firebasechat.common.Const
import com.nikasov.firebasechat.data.chat.Dialog
import com.google.firebase.firestore.ktx.toObject
import com.nikasov.firebasechat.util.Resource
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject
import kotlin.collections.ArrayList

class DialogRepository @Inject constructor(){

    private val profileCollection = Firebase.firestore.collection(Const.PROFILE_COLLECTION)
    private val dialogsCollection = Firebase.firestore.collection(Const.DIALOG_COLLECTION)

    suspend fun addDialog(owner: String, member: String, memberName : String) : Resource<String?>{
        try {
            var id : String?
            val members : ArrayList<String> = arrayListOf()

            members.apply {
                add(owner)
                add(member)
            }

            dialogsCollection.add(
                Dialog(
                    members,
                    memberName,
                    owner)
            ).also { doc ->
                doc.await()
                id = doc.result?.id
                members.forEach { member ->
                    profileCollection.document(member)
                        .collection(Const.DIALOG_COLLECTION)
                        .document(id!!)
                        .set(Dialog())
                        .await()
                }
            }
            return Resource.Success(id)
        } catch (e: Exception) {
            return Resource.Error(e.localizedMessage)
        }
    }

    suspend fun getDialogs(owner: String) : List<Dialog> {
        return try {
            val dialogs = arrayListOf<Dialog>()
            val dialogsIds =
                profileCollection
                    .document(owner)
                    .collection(Const.DIALOG_COLLECTION)
                    .get().await()

            dialogsIds.forEach { dialogId ->
                dialogs.add(
                    dialogsCollection
                    .document(dialogId.id)
                    .get()
                    .await()
                    .toObject<Dialog>()!!
                )
            }
            return dialogs
        } catch (e: Exception) {
            emptyList()
        }
    }
}