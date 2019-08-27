package mtaubert.loginapplication


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import mtaubert.loginapplication.Model.User
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
            if(inputsAreValid(binding.nameEntry.text.toString(), binding.emailEntry.text.toString(), binding.passwordEntry.text.toString())) {
                view.findNavController().navigate(R.id.action_signUpFragment_to_accountFragment)
            }
        }
        return binding.root
    }

    private fun inputsAreValid(name:String, email:String, password:String):Boolean {
        if(name.replace(" ", "") == "") {
            Toast.makeText(activity, "Your name can not be empty!", Toast.LENGTH_LONG).show()
            return false
        }

        if(!email.contains("@") || !email.contains(".") || email.contains(" ")) {
            Toast.makeText(activity, "Your email is not a valid email!", Toast.LENGTH_LONG).show()
            return false
        }

        if(password.length <= 7) {
            Toast.makeText(activity, "Your password must be at least 8 characters long!", Toast.LENGTH_LONG).show()
            return false
        }else if (password.contains(Regex("[^A-Za-z0-9!@#$&]"))) {
            return false
        }

        val user = User()
        user.name = name
        user.email = email
        user.password = password

        if((activity as LoginActivity).db.doesUserExist(user)) {
            Toast.makeText(activity, "Your email is already registered!", Toast.LENGTH_LONG).show()
            return false
        }

        (activity as LoginActivity).db.addPerson(user)
        (activity as LoginActivity).currentUser = user
        return true
    }
}
