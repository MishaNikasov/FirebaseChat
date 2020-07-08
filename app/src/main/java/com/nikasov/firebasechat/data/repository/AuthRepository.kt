package com.nikasov.firebasechat.data.repository

import android.content.Context
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.nikasov.firebasechat.common.Const
import com.nikasov.firebasechat.data.user.Profile
import com.nikasov.firebasechat.util.AuthResource
import kotlinx.coroutines.tasks.await
import com.nikasov.firebasechat.util.Resource
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth
) {

    private val profileCollection = Firebase.firestore.collection(Const.PROFILE_COLLECTION)

    suspend fun firebaseLogInWithGoogle(
        googleAuthCredential: AuthCredential
    ) : AuthResource<Profile>{
        return try {
            firebaseAuth.signInWithCredential(googleAuthCredential).await()
            if (checkIsExist(firebaseAuth.uid) != null){
                AuthResource.LogIn(checkIsExist(firebaseAuth.uid))
            } else {
                saveProfile(Profile(
                    firebaseAuth.uid,
                    firebaseAuth.currentUser?.displayName.toString(),
                    firebaseAuth.currentUser?.email.toString()
                ))
                AuthResource.SignUp(checkIsExist(firebaseAuth.uid))
            }
        } catch (e: Exception) {
            AuthResource.Error(e.localizedMessage)
        }
    }

    suspend fun logInWithEmail(
        email: String,
        password: String
    ) : AuthResource<Profile> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(
                email,
                password
            ).await()
            AuthResource.SignUp(checkIsExist(firebaseAuth.uid))
        } catch (e: Exception) {
            return AuthResource.Error(e.localizedMessage)
        }
    }

    suspend fun signUpWithEmail(
        name: String,
        email : String,
        password : String
    ) : AuthResource<Profile> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(
                email,
                password
            ).await()
            saveProfile(Profile(firebaseAuth.uid, name, email))
            AuthResource.SignUp(checkIsExist(firebaseAuth.uid))
        } catch (e: Exception) {
            return AuthResource.Error(e.localizedMessage)
        }
    }

    suspend fun checkIsExist(uid : String?) : String? {
        return try {
            val profileQuery = profileCollection.whereEqualTo("uid", uid)
                .get()
                .await()
            if (profileQuery.documents.first().toObject<Profile>() != null){
                profileQuery.documents.first().toObject<Profile>()!!.id
            } else {
                null
            }
        } catch (e : java.lang.Exception) {
            null
        }
    }

    private suspend fun saveProfile(profile: Profile) {
        try {
            profileCollection.add(profile).await()
        } catch (e: java.lang.Exception) {

        }
    }
}