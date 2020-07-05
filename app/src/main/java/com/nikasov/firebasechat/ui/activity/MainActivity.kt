package com.nikasov.firebasechat.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.GoogleAuthProvider
import com.nikasov.firebasechat.R
import com.nikasov.firebasechat.common.Const.GOOGLE_SIGN_IN_REQUEST
import com.nikasov.firebasechat.ui.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_activity.*
import timber.log.Timber
import java.lang.Exception

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val authViewModel : AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        authViewModel.setCurrentUser()
        authViewModel.currentUser.observe(this, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    loadingScreen.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    resource.data?.let {
                        goToProfile()
                    }
                    loadingScreen.visibility = View.GONE
                }
            }
        })
    }

    private fun googleAuthToFirebase(signInAccount: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(signInAccount.idToken, null)
        authViewModel.signInWithGoogle(credentials)
    }

    private fun goToProfile() {
        hostFragment.findNavController().apply {
            popBackStack()
            navigate(R.id.toProfileFragment)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN_REQUEST) {
            try {
                val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
                account?.let {
                    googleAuthToFirebase(it)
                }
            } catch (e : Exception) {
                Timber.d(e.localizedMessage)
            }
        }
    }
}