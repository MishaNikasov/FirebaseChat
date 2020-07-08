package com.nikasov.firebasechat.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.nikasov.firebasechat.R
import com.nikasov.firebasechat.util.Prefs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs : Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        checkIsLogged()
    }

    private fun checkIsLogged() {
        if (prefs.loadProfileId() != null) {
            hostFragment.findNavController().apply {
                popBackStack()
                navigate(R.id.action_authFragment_to_profileFragment)
            }
        }
    }

}