package com.nikasov.firebasechat.data.repository

import android.content.Context
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
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

    val profileCollection = Firebase.firestore.collection(Const.PROFILE_COLLECTION)

    suspend fun firebaseLogInWithGoogle(
        googleAuthCredential: AuthCredential
    ) : AuthResource<Profile>{
        return try {
            if (true){
                firebaseAuth.signInWithCredential(googleAuthCredential).await()
                AuthResource.LogIn(firebaseAuth.uid)
            } else {
                firebaseAuth.signInWithCredential(googleAuthCredential).await()
                AuthResource.SignUp(Profile(
                    firebaseAuth.uid,
                    firebaseAuth.currentUser?.displayName.toString(),
                    firebaseAuth.currentUser?.email.toString()
                ))
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
            AuthResource.LogIn(firebaseAuth.uid)
        } catch (e: Exception) {
            return AuthResource.Error(e.localizedMessage)
        }
    }//todo: прокидывать айдишник в аутфраг что бы передать его аргументов в профиль

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
            AuthResource.SignUp(Profile(firebaseAuth.uid, name, email))
        } catch (e: Exception) {
            return AuthResource.Error(e.localizedMessage)
        }
    }

}