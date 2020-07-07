package com.nikasov.firebasechat.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.nikasov.firebasechat.common.Const
import com.nikasov.firebasechat.data.user.Profile
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val auth : FirebaseAuth
) {

    private val profileCollection = Firebase.firestore.collection(Const.PROFILE_COLLECTION)

    suspend fun saveProfile(profile: Profile) {
        try {
            profileCollection.add(profile).await()
        } catch (e: Exception) {

        }
    }

    suspend fun getProfile() : Profile{
        val whereId = auth.uid
        val profileQuery = profileCollection
            .whereEqualTo("uid", whereId!!)
            .get()
            .await()
        return profileQuery.first().toObject()
    }
}