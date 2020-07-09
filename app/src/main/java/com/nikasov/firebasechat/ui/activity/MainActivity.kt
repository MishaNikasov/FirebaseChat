package com.nikasov.firebasechat.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.nikasov.firebasechat.R
import com.nikasov.firebasechat.util.Prefs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs : Prefs
    @Inject
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        setUpNavigation()
        setUpToolbar()
        checkIsLogged()
    }

    private fun setUpNavigation() {
        hostFragment.findNavController().addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.authFragment -> {
                    toolbar.visibility = View.GONE
                }
                else -> {
                    toolbar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setUpToolbar() {
        toolbar.title = null
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logOut -> {
                    logOut()
                    true
                }
                R.id.contacts -> {
                    goToContacts()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun logOut() {
        auth.signOut()
        prefs.clearUser()
        hostFragment.findNavController().apply {
            popBackStack()
            navigate(R.id.toAuthFragment)
        }
    }

    private fun goToContacts() {
        hostFragment.findNavController().navigate(R.id.action_contactFragment_self)
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