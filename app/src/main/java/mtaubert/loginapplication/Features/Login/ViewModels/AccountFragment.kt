package mtaubert.loginapplication.Features.Login.ViewModels


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mtaubert.loginapplication.Features.Login.Models.LoginModel
import mtaubert.loginapplication.R
import mtaubert.loginapplication.databinding.FragmentAccountBinding

class AccountFragment(private val loginModel: LoginModel) : Fragment() {

    companion object {
        fun newInstance(loginModel: LoginModel): AccountFragment {
            return AccountFragment(loginModel)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAccountBinding>(inflater,
            R.layout.fragment_account,container,false)
        //The complete onClickListener with Navigation

        //No back button on the tool bar
        (activity as LoginActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(false)

        //Gets the user's name to display at the top of the fragment
        val user = loginModel.currentUser
        binding.nameDisplay.append(" " + user?.name)

        //Adds button listeners
        setupButtons(binding)

        return binding.root
    }

    /**
     * Sets up button listeners
     */
    private fun setupButtons(binding: FragmentAccountBinding) {
        //Logs the user out
        binding.logoutButton.setOnClickListener {
            loginModel.currentUser = null
            (activity as LoginActivity).changeFragment("login", loginModel)
        }

        //Moves the user to the account details fragment
        binding.accountDetailsButton.setOnClickListener {
            (activity as LoginActivity).changeFragment("accountDetails", loginModel)
        }

        //Deletes the account from the db and logs the user out
        binding.deleteAccountButton.setOnClickListener {
            GlobalScope.launch {
                (activity as LoginActivity).db.userDao().deleteUser(loginModel.currentUser!!)
            }

            loginModel.currentUser = null
            (activity as LoginActivity).changeFragment("login", loginModel)
        }
    }
}
