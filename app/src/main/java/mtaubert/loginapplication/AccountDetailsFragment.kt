package mtaubert.loginapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mtaubert.loginapplication.Data.User
import mtaubert.loginapplication.databinding.FragmentAccountDetailsBinding

class AccountDetailsFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAccountDetailsBinding>(inflater,
                R.layout.fragment_account_details,container,false)

        (activity as LoginActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        populateEditTexts(binding)

        setButtonListeners(binding)

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                (activity as LoginActivity).onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun populateEditTexts(binding:FragmentAccountDetailsBinding) {
        val user = (activity as LoginActivity).currentUser
        if (user != null) {
            binding.nameInput.setText(user.name)
            binding.emailInput.setText(user.email)
            binding.passwordInput.setText(user.password)
        }
    }

    private fun setButtonListeners(binding:FragmentAccountDetailsBinding) {
        binding.saveButton.setOnClickListener {
            val userDao = (activity as LoginActivity).db.userDao()
            GlobalScope.launch {
                val users = userDao.getUser(binding.emailInput.text.toString())
                val  updatedUser = User(binding.emailInput.text.toString(), binding.passwordInput.text.toString(), binding.nameInput.text.toString())
                if(users.isEmpty()) {
                    userDao.deleteUser((activity as LoginActivity).currentUser!!)
                    userDao.insert(updatedUser)
                } else {
                    userDao.updateUser(updatedUser)
                }
                (activity as LoginActivity).currentUser = updatedUser
            }
        }
    }
}
