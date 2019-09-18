package mtaubert.loginapplication.Features.Login.Views


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.runBlocking
import mtaubert.loginapplication.Features.API.Views.APIActivity
import mtaubert.loginapplication.R
import mtaubert.loginapplication.Utils.Fragments.BaseLoginFragment
import mtaubert.loginapplication.databinding.LoginFragmentDashboardBinding
import java.lang.NullPointerException

class DashboardFragment : BaseLoginFragment() {

    companion object {
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<LoginFragmentDashboardBinding>(
            inflater,
            R.layout.login_fragment_dashboard, container, false
        )

        //No back button on the tool bar
        (activity as LoginActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(true)

        populateNameDisplayWithUserName(binding)
        setupButtonListeners(binding)

        return binding.root
    }

    private fun populateNameDisplayWithUserName(binding: LoginFragmentDashboardBinding) {
        try {
            binding.nameDisplay.append(" " + loginViewModel.getCurrentUser()!!.name)
        } catch (e: NullPointerException) { //User somehow got to the screen without logging into the app
            Toast.makeText(activity, "Error getting user details, returning to the login page", Toast.LENGTH_SHORT).show()
            logoutAndGoToLoginFragment()
        }

    }

    private fun setupButtonListeners(binding: LoginFragmentDashboardBinding) {
        binding.logoutButton.setOnClickListener {
            logoutAndGoToLoginFragment()
        }

        binding.accountDetailsButton.setOnClickListener {
            (activity as LoginActivity).changeFragment("accountDetails")
        }

        binding.apiButton.setOnClickListener {
            val intent = Intent((activity as LoginActivity), APIActivity::class.java)
            intent.putExtra("currentUser", loginViewModel.getCurrentUser())
            startActivity(intent)
        }

        binding.deleteAccountButton.setOnClickListener {
            deleteCurrentUserAccountAndLogOut()
        }
    }

    private fun deleteCurrentUserAccountAndLogOut() = runBlocking {
        Toast.makeText(activity, "Deleted user ${loginViewModel.getCurrentUser()?.name}", Toast.LENGTH_SHORT).show()
        loginViewModel.deleteUsersFromDB(arrayListOf(loginViewModel.getCurrentUser()!!))
        logoutAndGoToLoginFragment()
    }

    private fun logoutAndGoToLoginFragment() {
        loginViewModel.logoutCurrentUser()
        (activity as LoginActivity).changeFragment("login")
    }

}
