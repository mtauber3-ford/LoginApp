package mtaubert.loginapplication

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mtaubert.loginapplication.Data.User
import mtaubert.loginapplication.RoomDatabase.UserRoomDatabase
import mtaubert.loginapplication.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var db:UserRoomDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(inflater,
            R.layout.fragment_login,container,false)

        db = (activity as LoginActivity).db

        //Sign up button goes to the sign up fragments
        binding.signUpButton.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        //Checks if the login credentials are valid and goes to the account fragment
        binding.loginButton.setOnClickListener { view : View ->
            loginValidation(binding.usernameInput.text.toString(), binding.passwordInput.text.toString())
        }

        //db = (activity as LoginActivity).db

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.admin_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.admin -> view?.findNavController()?.navigate(R.id.action_loginFragment_to_adminFragment)
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    /**
    Validates the login details and saves the current user if details are correct and a user is returned
     */
    private fun loginValidation(username:String, password:String) {
        if(username != "" && password != "") {
            GlobalScope.launch {
                val listOfUsers = db.userDao().getUser(username)
                if(listOfUsers.isNotEmpty()) {
                    if(listOfUsers[0].password == password) {
                        loginSuccess(listOfUsers[0])
                    } else {
                        loginFailure()
                    }
                } else {
                    loginFailure()
                }
            }
        } else {
            Toast.makeText(activity, "Enter login credentials!", Toast.LENGTH_LONG).show()
        }

    }

    private fun loginSuccess(user:User) {
        (activity as LoginActivity).currentUser = user
        view?.findNavController()?.navigate(R.id.action_loginFragment_to_accountFragment)
    }

    private fun loginFailure() {
        activity?.runOnUiThread(Runnable { Toast.makeText(activity, "Username and/or password incorrect!", Toast.LENGTH_LONG).show() })
    }
}
