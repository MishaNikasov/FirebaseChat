package com.nikasov.firebasechat.ui.fragment.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.GoogleAuthProvider
import com.nikasov.firebasechat.R
import com.nikasov.firebasechat.common.Const.GOOGLE_SIGN_IN_REQUEST
import com.nikasov.firebasechat.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_auth.*
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val viewModel: AuthViewModel by viewModels()
    private var isLogInState : MutableLiveData<Boolean> = MutableLiveData(false)

    @Inject
    lateinit var signInOption : GoogleSignInClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        viewModel.setCurrentUser()
        viewModel.currentUser.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    loading(true)
                }
                is Resource.Success -> {
                    loading(false)
                    resource.data?.let { uid ->
                        goToProfile(uid)
                    }
                }
                is Resource.Empty -> {
                    loading(false)
                }
                is Resource.Error -> {
                    loading(false)
                    showAlert(resource.message)
                }
            }
        })

        stateBtn.setOnClickListener {
            isLogInState.postValue(!isLogInState.value!!)
        }

        isLogInState.observe(viewLifecycleOwner, Observer {
            if (it) {
                motionLayout.transitionToState(R.id.stateBtn)
                signUpBtn.text = resources.getString(R.string.login)
                signUpGoogle.text = resources.getString(R.string.login_with_google)
                stateBtn.text = resources.getString(R.string.signin)
            } else {
                motionLayout.transitionToState(R.id.signUp)
                signUpBtn.text = resources.getString(R.string.signin)
                signUpGoogle.text = resources.getString(R.string.signin_with_google)
                stateBtn.text = resources.getString(R.string.login)
            }
        })

        signUpBtn.setOnClickListener {
            registerUser()
        }
        signUpGoogle.setOnClickListener {
            signInByGoogle()
        }
    }

    private fun validateName(): Boolean {
        val nameTxt = nameInput.text.toString()
        return if (nameTxt.isEmpty()) {
            textInputName.error = "Field can't be empty"
            false
        } else {
            textInputName.error = null
            true
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

    private fun registerUser() {
        if (!isLogInState.value!! && validateName() && validateEmail() && validatePassword()) {
            viewModel.signUpWithEmail(
                nameInput.text.toString(),
                emailInput.text.toString(),
                passwordInput.text.toString()
            )
        } else if (isLogInState.value!! && validateEmail() && validatePassword()) {
            viewModel.logInWithEmail(
                emailInput.text.toString(),
                passwordInput.text.toString()
            )
        }
    }

    private fun signInByGoogle() {
        signInOption.signInIntent.also {
            startActivityForResult(it, GOOGLE_SIGN_IN_REQUEST)
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

    private fun googleAuthToFirebase(signInAccount: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(signInAccount.idToken, null)
        viewModel.signInWithGoogle(credentials)
    }

    private fun goToProfile(uid : String) {
        if (uid.isNotEmpty()) {
            findNavController().apply {
                popBackStack()
                navigate(AuthFragmentDirections.actionAuthFragmentToProfileFragment(uid))
            }
        } else {
            findNavController().apply {
                popBackStack()
                navigate(R.id.action_authFragment_to_profileFragment)
            }
        }
    }

    private fun loading (isShow : Boolean) {
        if (isShow) {
            loadScreen.visibility = View.VISIBLE
        } else {
            loadScreen.visibility = View.INVISIBLE
        }
    }

    private fun showAlert(message: String?) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Something wrong")
            .setMessage(message)
            .setPositiveButton("OK") { _, _ -> }
            .show()
    }
}