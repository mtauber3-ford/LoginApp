package mtaubert.loginapplication.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mtaubert.loginapplication.Activities.LoginActivity
import mtaubert.loginapplication.R
import mtaubert.loginapplication.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {
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
        val user = (activity as LoginActivity).currentUser
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
        binding.logoutButton.setOnClickListener { view : View ->
            (activity as LoginActivity).currentUser = null
            view.findNavController().navigate(R.id.action_accountFragment_to_loginFragment)
        }

        //Moves the user to the account details fragment
        binding.accountDetailsButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_accountFragment_to_accountDetailsFragment)
        }

        //Deletes the account from the db and logs the user out
        binding.deleteAccountButton.setOnClickListener { view: View ->
            GlobalScope.launch {
                (activity as LoginActivity).db.userDao().deleteUser((activity as LoginActivity).currentUser!!)
            }

            (activity as LoginActivity).currentUser = null
            view.findNavController().navigate(R.id.action_accountFragment_to_loginFragment)
        }
    }
}
