package mtaubert.loginapplication.Features.Login.Views

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.runBlocking
import mtaubert.loginapplication.Utils.Fragments.BaseLoginFragment
import mtaubert.loginapplication.databinding.LoginFragmentLoginBinding

class LoginFragment : BaseLoginFragment() {

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }

    override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = DataBindingUtil.inflate<LoginFragmentLoginBinding>(
            inflater,
            mtaubert.loginapplication.R.layout.login_fragment_login, container, false
        )

        //Show options menu
        setHasOptionsMenu(true)

        //Log in button
        //Verifies the login details
        binding.loginButton.setOnClickListener {
            loginUser(binding.usernameInput.text.toString(), binding.passwordInput.text.toString())
        }

        //Sign up button
        //Goes to the sign up fragment
        binding.signUpButton.setOnClickListener() {
            (activity as LoginActivity).changeFragment("signUp")
        }

        checkIfUserLoggedIn()

        return binding.root
    }

    private fun checkIfUserLoggedIn() {
        if(loginViewModel.getCurrentUser() != null){
            Toast.makeText(activity, "Logged out ${loginViewModel.getCurrentUser()?.name}", Toast.LENGTH_LONG).show()
            loginViewModel.logoutCurrentUser()
        }
    }

    /**
     * Login button functionality
     */
    private fun loginUser(email: String, password: String) = runBlocking {
        val loginValid = loginViewModel.validateUserLoginDetails(email, password)

        if(loginValid) {
            (activity as LoginActivity).changeFragment("dashboard")
        } else {
            Toast.makeText(activity, "Login details invalid. Try again!", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Options menu setup and listener
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(mtaubert.loginapplication.R.menu.admin_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            mtaubert.loginapplication.R.id.admin -> (activity as LoginActivity).changeFragment("admin")
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}
