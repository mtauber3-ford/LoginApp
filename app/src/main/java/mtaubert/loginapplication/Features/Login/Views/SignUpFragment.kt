package mtaubert.loginapplication.Features.Login.Views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.runBlocking
import mtaubert.loginapplication.R
import mtaubert.loginapplication.Utils.Fragments.BaseLoginFragment
import mtaubert.loginapplication.databinding.FragmentSignUpBinding

class SignUpFragment : BaseLoginFragment() {

    companion object {
        fun newInstance(): SignUpFragment {
            return SignUpFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentSignUpBinding>(inflater,
            R.layout.login_fragment_sign_up,container,false)

        setupButtons(binding)

        return binding.root
    }

    /**
     * Sets up the button on click listeners
     */
    private fun setupButtons(binding:FragmentSignUpBinding) {

        //returns the user to the login fragment
        binding.loginButton.setOnClickListener {
            (activity as LoginActivity).changeFragment("login")
        }

        //Verifies the users inputs
        //Moves to the dashboard if details are valid
        binding.signUpButton.setOnClickListener {
            signUpUser(binding.nameEntry.text.toString(), binding.emailEntry.text.toString(), binding.passwordEntry.text.toString()) //Validates the info the user gave
        }
    }

    /**
     * Sign up the new user and log them in if the details are ok
     * Shows error toast if something goes wrong
     */
    private fun signUpUser(name: String, email: String, password: String) = runBlocking {
        val detailsValid = loginViewModel.validateUserSignUpDetails(email, password, name)

        if(detailsValid == null) {
            (activity as LoginActivity).changeFragment("dashboard")
        } else {
            Toast.makeText(activity, detailsValid, Toast.LENGTH_LONG).show()
        }
    }
}
