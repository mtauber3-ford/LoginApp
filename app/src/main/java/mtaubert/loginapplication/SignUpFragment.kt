package mtaubert.loginapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mtaubert.loginapplication.Data.User
import mtaubert.loginapplication.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSignUpBinding>(inflater,
            R.layout.fragment_sign_up,container,false)
        //The complete onClickListener with Navigation
        binding.loginButton.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        binding.signUpButton.setOnClickListener { view : View ->
            validateInputs(binding.nameEntry.text.toString(), binding.emailEntry.text.toString(), binding.passwordEntry.text.toString())
        }
        return binding.root
    }

    private fun validateInputs(name:String, email:String, password:String) {
        if(name.replace(" ", "") == "") {
            Toast.makeText(activity, "Your name can not be empty!", Toast.LENGTH_LONG).show()
        }

        if(!email.contains("@") || !email.contains(".") || email.contains(" ")) {
            Toast.makeText(activity, "Your email is not a valid email!", Toast.LENGTH_LONG).show()
        }

        if(password.length <= 7) {
            Toast.makeText(activity, "Your password must be at least 8 characters long!", Toast.LENGTH_LONG).show()
        }else if (password.contains(Regex("[^A-Za-z0-9!@#$&]"))) {
            Toast.makeText(activity, "Your password can only contain letters, numbers or these symbols: !@#$&", Toast.LENGTH_LONG).show()
        }

        val user = User(email, password, name)


        var userExists: Boolean
        GlobalScope.launch {
            userExists = (activity as LoginActivity).db.userDao().getUser(email).isNotEmpty()

            if(userExists) {
                activity?.runOnUiThread(Runnable {
                    Toast.makeText(activity, "Your email is already registered!", Toast.LENGTH_LONG).show()
                })
            } else {
                (activity as LoginActivity).db.userDao().insert(user)
                (activity as LoginActivity).currentUser = user
                view!!.findNavController().navigate(R.id.action_signUpFragment_to_accountFragment)
            }
        }
    }
}
