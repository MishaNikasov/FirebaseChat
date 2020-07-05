package com.nikasov.firebasechat.ui.fragment.signin

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.nikasov.firebasechat.R
import com.nikasov.firebasechat.common.ResourceProvider

class SignInViewModel @ViewModelInject constructor(
    private val resourceProvider: ResourceProvider
): ViewModel() {


}