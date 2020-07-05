package com.nikasov.firebasechat.ui.fragment.signin

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.nikasov.firebasechat.R
import com.nikasov.firebasechat.common.Const.GOOGLE_SIGN_IN_REQUEST
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_sign_in.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.lang.Exception
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.main_sign_in) {

    private val viewModel: SignInViewModel by viewModels()

    @Inject
    lateinit var auth : FirebaseAuth
    @Inject
    lateinit var signInOption : GoogleSignInClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        signInBtn.setOnClickListener {
            signIn()
        }
        signInGoogle.setOnClickListener {
            signInByGoogle()
        }
        logIn.setOnClickListener {
//            findNavController().navigate()
        }
    }

    private fun validateEmail(): Boolean {
        val emailTxt = emailInput.text.toString()
        return if (emailTxt.isEmpty()) {
            textInputEmail.error = "Field can't be empty"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches()) {
            textInputEmail.error = "Enter a valid email address"
            false
        } else {
            textInputEmail.error = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        val passText = passwordInput.text.toString()
        return when {
            passText.isEmpty() -> {
                textInputPassword.error = "Field can't be empty"
                false
            }
            passText.length < 6 -> {
                textInputPassword.error = "At least 6 characters"
                false
            }
            else -> {
                textInputPassword.error = null
                true
            }
        }
    }

    private fun signIn() {
        if (validateEmail() && validatePassword()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(
                        emailInput.text.toString(),
                        passwordInput.text.toString()
                    ).await()
                    findNavController().navigate(R.id.toProfileFragment)
                } catch (e : Exception) {
                    
                }
            }
        }
    }

    private fun signInByGoogle() {
        signInOption.signInIntent.also {
            requireActivity().startActivityForResult(it, GOOGLE_SIGN_IN_REQUEST)
        }
    }
}