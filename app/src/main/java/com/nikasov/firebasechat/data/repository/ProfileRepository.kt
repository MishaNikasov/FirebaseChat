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

    suspend fun getProfile(profileId: String) : Profile {
        val profileQuery = profileCollection
            .document(profileId)
            .get()
            .await()
        return profileQuery.toObject<Profile>()!!
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