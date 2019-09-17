package mtaubert.loginapplication.Features.Login.Views


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import mtaubert.loginapplication.R
import mtaubert.loginapplication.Utils.Fragments.BaseLoginFragment
import mtaubert.loginapplication.databinding.LoginFragmentDashboardBinding
import java.lang.NullPointerException

class Dashboard : BaseLoginFragment() {

    companion object {
        fun newInstance(): Dashboard {
            return Dashboard()
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
    }

    private fun logoutAndGoToLoginFragment() {
        loginViewModel.logoutCurrentUser()
        (activity as LoginActivity).changeFragment("login")
    }

}
