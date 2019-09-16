package mtaubert.loginapplication.Features.Login.Views

import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mtaubert.loginapplication.Data.DB.Model.User
import mtaubert.loginapplication.Data.DB.UserRoomDatabase
import mtaubert.loginapplication.Features.Login.Models.LoginModel
import mtaubert.loginapplication.Features.Login.ViewModels.LoginViewModel
import mtaubert.loginapplication.Utils.Fragments.BaseLoginFragment
import mtaubert.loginapplication.databinding.FragmentLoginBinding


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

        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(
            inflater,
            mtaubert.loginapplication.R.layout.fragment_login, container, false
        )
        binding.loginButton.setOnClickListener {
            loginUser(binding.usernameInput.text.toString(), binding.passwordInput.text.toString())
        }

        return binding.root
    }

    private fun loginUser(email: String, password: String) = runBlocking {
        val loginValid = async {
            loginViewModel.validateUserLoginDetails(email, password)
        }

        if(loginValid.await()) {
            (activity as LoginActivity).changeFragment("dashboard")
        } else {
            Toast.makeText(activity, "Login details invalid. Try again!", Toast.LENGTH_LONG).show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
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
//
//    private lateinit var db: UserRoomDatabase
//    private lateinit var loginViewModel: LoginModel
//
//    companion object {
//        fun newInstance(loginModel: LoginModel): LoginFragment {
//            val args = Bundle()
//            if (loginModel.currentUser != null) {
//                args.putString("email", loginModel.currentUser?.email)
//                args.putString("password", loginModel.currentUser?.password)
//                args.putString("name", loginModel.currentUser?.name)
//            }
//            val fragment = LoginFragment()
//            fragment.arguments = args
//
//            return LoginFragment()
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//
//        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(inflater,
//            mtaubert.loginapplication.R.layout.fragment_login,container,false)
//
//        db = (activity as LoginActivity).db
////        if(savedInstanceState != null) {
////            loginModel.currentUser = User(savedInstanceState.getString("email")!!, savedInstanceState.getString("password")!!, savedInstanceState.getString("name")!!)
////        }
//        //Logs out the current user if they get here
//        if (loginModel.currentUser != null) {
//            Toast.makeText(activity, "Logged out ${loginModel.currentUser!!.email}", Toast.LENGTH_LONG).show()
//           loginModel.currentUser = null
//        }
//
//        setupButtons(binding)
//
//        return binding.root
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater?.inflate(mtaubert.loginapplication.R.menu.admin_menu, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId) {
//            mtaubert.loginapplication.R.id.admin -> (activity as LoginActivity).changeFragment("admin", loginModel)
//            else -> return super.onOptionsItemSelected(item)
//        }
//        return true
//    }
//
//    /**
//     * Set up the button click listeners
//     */
//    private fun setupButtons(binding:FragmentLoginBinding) {
//        //Sign up button goes to the sign up fragment
//        binding.signUpButton.setOnClickListener {
//            (activity as LoginActivity).changeFragment("signUp", loginModel)
//        }
//
//        //Checks if the login credentials are valid and goes to the account fragment
//        binding.loginButton.setOnClickListener {
//            loginValidation(binding.usernameInput.text.toString(), binding.passwordInput.text.toString())
//        }
//
//        //Make the options menu visible
//        setHasOptionsMenu(true)
//    }
//
//    /**
//    Validates the login details and saves the current user if details are correct and a user is returned
//     */
//    private fun loginValidation(username:String, password:String) {
//        if(username != "" && password != "") { //Username and password fields cannot be empty
//            GlobalScope.launch {
//                val listOfUsers = db.userDao().getUser(username) //grabs users from the db
//                if(listOfUsers.isNotEmpty()) { //There are users found
//                    if(listOfUsers[0].password == password) { //First user (there should only be one at most in this scenario)
//                        loginSuccess(listOfUsers[0]) //Successful login, provides user details to pass on
//                    } else { //Password does not match
//                        loginFailure()
//                    }
//                } else { //Email was not found
//                    loginFailure()
//                }
//            }
//        } else { //Error warning to enter some details before hitting the button
//            Toast.makeText(activity, "Enter login credentials!", Toast.LENGTH_LONG).show()
//        }
//
//    }
//
//    /**
//     * Successful login, goes to the account fragment
//     */
//    private fun loginSuccess(user: User) {
//        loginModel .currentUser = user //Sets the current user
//        (activity as LoginActivity).changeFragment("dashboard", loginModel)
//    }
//
//    /**
//     * Something went wrong with the login
//     */
//    private fun loginFailure() {
//        activity?.runOnUiThread{ Toast.makeText(activity, "Username and/or password incorrect!", Toast.LENGTH_LONG).show() }
//    }
}
