package com.nikasov.firebasechat.ui.fragment.signin

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.nikasov.firebasechat.R
import com.nikasov.firebasechat.common.Const.GOOGLE_SIGN_IN_REQUEST
import com.nikasov.firebasechat.ui.activity.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val viewModel: AuthViewModel by viewModels()
    private var isLogInState : MutableLiveData<Boolean> = MutableLiveData(false)

    @Inject
    lateinit var auth : FirebaseAuth
    @Inject
    lateinit var signInOption : GoogleSignInClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {

        logIn.setOnClickListener {
            isLogInState.postValue(!isLogInState.value!!)
        }

        isLogInState.observe(viewLifecycleOwner, Observer {
            if (it) {
                motionLayout.transitionToState(R.id.logIn)
                signInBtn.text = resources.getString(R.string.login)
                signInGoogle.text = resources.getString(R.string.login_with_google)
                logIn.text = resources.getString(R.string.signin)
            } else {
                motionLayout.transitionToState(R.id.signIn)
                signInBtn.text = resources.getString(R.string.signin)
                signInGoogle.text = resources.getString(R.string.signin_with_google)
                logIn.text = resources.getString(R.string.login)
            }
        })

        signInBtn.setOnClickListener {
            signIn()
        }
        signInGoogle.setOnClickListener {
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

    private fun signIn() {
        if (!isLogInState.value!! && validateName() && validateEmail() && validatePassword()) {
            viewModel.signInWithEmail(
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
            requireActivity().startActivityForResult(it, GOOGLE_SIGN_IN_REQUEST)
        }
    }

    private fun showAlert(message: String?) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Something wrong")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, which -> }
            .show()
    }
}