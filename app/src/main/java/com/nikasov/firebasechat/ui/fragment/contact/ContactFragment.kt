package com.nikasov.firebasechat.ui.fragment.contact

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.nikasov.firebasechat.R
import com.nikasov.firebasechat.data.user.Profile
import com.nikasov.firebasechat.ui.adapter.ContactAdapter
import com.nikasov.firebasechat.util.Prefs
import com.nikasov.firebasechat.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_contact.*
import javax.inject.Inject

@AndroidEntryPoint
class ContactFragment : Fragment(R.layout.fragment_contact) {

    private val viewModel : ContactViewModel by viewModels()

    @Inject
    lateinit var prefs : Prefs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
    }

    private fun initUi() {
        setUpContactList()
    }

    private fun setUpContactList() {
        viewModel.getAllContacts()

        val onContactClickListener = object : ContactAdapter.Interaction {
            override fun onItemSelected(position: Int, item: Profile) {
                openChat(item.id)
            }
        }

        val contactAdapter = ContactAdapter(onContactClickListener)

        contactRecycler.apply {
            adapter = contactAdapter
        }

        viewModel.contacts.observe(viewLifecycleOwner, Observer {  list ->
            contactAdapter.submitList(list)
        })
    }

    fun openChat(member : String) {
        val action =
            ContactFragmentDirections.actionContactFragmentToChatFragment(member)
        findNavController().apply {
            navigate(action)
        }
    }
}