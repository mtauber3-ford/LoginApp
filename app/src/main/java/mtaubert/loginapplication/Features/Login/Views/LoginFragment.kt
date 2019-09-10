package mtaubert.loginapplication.Features.Login.Views

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mtaubert.loginapplication.Data.DB.Model.User
import mtaubert.loginapplication.R
import mtaubert.loginapplication.Data.DB.UserRoomDatabase
import mtaubert.loginapplication.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var db: UserRoomDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(inflater,
            R.layout.fragment_login,container,false)

        db = (activity as LoginActivity).db

        //Logs out the current user if they get here
        if ((activity as LoginActivity).currentUser != null) {
            Toast.makeText(activity, "Logged out ${(activity as LoginActivity).currentUser!!.email}", Toast.LENGTH_LONG).show()
            (activity as LoginActivity).currentUser = null
        }

        setupButtons(binding)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.admin_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.admin -> view?.findNavController()?.navigate(R.id.action_loginFragment_to_adminFragment) //goes to the admin fragment
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    /**
     * Set up the button click listeners
     */
    private fun setupButtons(binding:FragmentLoginBinding) {
        //Sign up button goes to the sign up fragment
        binding.signUpButton.setOnClickListener {
            it!!.findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        //Checks if the login credentials are valid and goes to the account fragment
        binding.loginButton.setOnClickListener {
            loginValidation(binding.usernameInput.text.toString(), binding.passwordInput.text.toString())
        }

        //Make the options menu visible
        setHasOptionsMenu(true)
    }

    /**
    Validates the login details and saves the current user if details are correct and a user is returned
     */
    private fun loginValidation(username:String, password:String) {
        if(username != "" && password != "") { //Username and password fields cannot be empty
            GlobalScope.launch {
                val listOfUsers = db.userDao().getUser(username) //grabs users from the db
                if(listOfUsers.isNotEmpty()) { //There are users found
                    if(listOfUsers[0].password == password) { //First user (there should only be one at most in this scenario)
                        loginSuccess(listOfUsers[0]) //Successful login, provides user details to pass on
                    } else { //Password does not match
                        loginFailure()
                    }
                } else { //Email was not found
                    loginFailure()
                }
            }
        } else { //Error warning to enter some details before hitting the button
            Toast.makeText(activity, "Enter login credentials!", Toast.LENGTH_LONG).show()
        }

    }

    /**
     * Successful login, goes to the account fragment
     */
    private fun loginSuccess(user: User) {
        (activity as LoginActivity).currentUser = user //Sets the current user
        view?.findNavController()?.navigate(R.id.action_loginFragment_to_accountFragment)
    }

    /**
     * Something went wrong with the login
     */
    private fun loginFailure() {
        activity?.runOnUiThread{ Toast.makeText(activity, "Username and/or password incorrect!", Toast.LENGTH_LONG).show() }
    }
}
