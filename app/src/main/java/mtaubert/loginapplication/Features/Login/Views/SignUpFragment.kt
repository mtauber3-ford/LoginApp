package mtaubert.loginapplication.Features.Login.Views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mtaubert.loginapplication.Data.DB.Model.User
import mtaubert.loginapplication.Features.Login.Models.LoginModel
import mtaubert.loginapplication.R
import mtaubert.loginapplication.databinding.FragmentSignUpBinding

class SignUpFragment(private val loginModel: LoginModel) : Fragment() {

    companion object {
        fun newInstance(loginModel: LoginModel): SignUpFragment {
            return SignUpFragment(loginModel)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSignUpBinding>(inflater,
            R.layout.fragment_sign_up,container,false)

        setupButtons(binding)

        return binding.root
    }

    /**
     * Sets up the button on click listeners
     */
    private fun setupButtons(binding:FragmentSignUpBinding) {
        //The complete onClickListener with Navigation
        binding.loginButton.setOnClickListener {
            (activity as LoginActivity).changeFragment("login")
        }
        binding.signUpButton.setOnClickListener {
            validateInputs(binding.nameEntry.text.toString(), binding.emailEntry.text.toString(), binding.passwordEntry.text.toString()) //Validates the info the user gave
        }
    }

    /**
     * Checks to make sure the inputs are valid before proceeding
     */
    private fun validateInputs(name:String, email:String, password:String) {
        if(name.replace(" ", "") == "") { //Name field was left empty or full of spaces
            Toast.makeText(activity, "Your name can not be empty!", Toast.LENGTH_LONG).show()
        }

        if(!email.contains("@") || !email.contains(".") || email.contains(" ")) { //Email must contain both @ and . to be a valid email, also cannot contain a space
            Toast.makeText(activity, "Your email is not a valid email!", Toast.LENGTH_LONG).show()
        }

        if(password.length <= 7) { //Password has to be at least 8  characters
            Toast.makeText(activity, "Your password must be at least 8 characters long!", Toast.LENGTH_LONG).show()
        }else if (password.contains(Regex("[^A-Za-z0-9!@#$&]"))) { //Any character outside this set is not valid for passwords
            Toast.makeText(activity, "Your password can only contain letters, numbers or these symbols: !@#$&", Toast.LENGTH_LONG).show()
        }

        val user =
            User(
                email,
                password,
                name
            ) //Creates a new user object using the input


        GlobalScope.launch {
            val userExists = (activity as LoginActivity).db.userDao().getUser(email).isNotEmpty() //Comes back true if the email already exists in the DB

            if(userExists) { //Display error message for the user that they're trying to register an existing account
                activity?.runOnUiThread{
                    Toast.makeText(activity, "Your email is already registered!", Toast.LENGTH_LONG).show()
                }
            } else { //Saves the user to the DB, sets currentUser to the one created and moves to the account Fragment
                (activity as LoginActivity).db.userDao().insert(user)
                loginModel.currentUser = user
                (activity as LoginActivity).changeFragment("dashboard")
            }
        }
    }
}
