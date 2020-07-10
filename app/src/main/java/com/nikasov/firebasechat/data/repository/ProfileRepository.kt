package com.nikasov.firebasechat.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.nikasov.firebasechat.common.Const
import com.nikasov.firebasechat.data.user.Profile
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val auth : FirebaseAuth
) {

    private val profileCollection = Firebase.firestore.collection(Const.PROFILE_COLLECTION)
    private val hashCollection = Const.HASH_COLLECTION

    suspend fun getProfile(profileId: String) : Profile {
        val profileQuery = profileCollection
            .document(profileId)
            .get()
            .await()
        return profileQuery.toObject<Profile>()!!
    }

    suspend fun ifChatExist(profileId: String, memberId: String) : String? {
        profileCollection
            .document(profileId)
            .collection(hashCollection)
            .document("$profileId$memberId")
            .get()
            .await().also {
                val doc = it.toObject<HashMap<String, String>>()
                doc?.let {
                    return it[memberId]
                }
            }
        return null
    }

    suspend fun addChatToProfile(profileId: String, memberId: String, chatId : String) {
        val profileHashMap : HashMap<String, String> = hashMapOf()
        profileHashMap[memberId] = chatId
        profileCollection
            .document(profileId)
            .collection(hashCollection)
            .document("$profileId$memberId")
            .set(profileHashMap)
            .await()

        val memberHashMap : HashMap<String, String> = hashMapOf()
        memberHashMap[profileId] = chatId
        profileCollection
            .document(memberId)
            .collection(hashCollection)
            .document("$memberId$profileId")
            .set(memberHashMap)
            .await()
    }

    suspend fun getAllProfiles(): List<Profile> {
        val profilesArray = arrayListOf<Profile>()

        profileCollection.get().await().also { profiles ->
            profiles.forEach { profile ->
                profilesArray.add(profile.toObject())
            }
        }

        return profilesArray
    }
}