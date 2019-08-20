package mtaubert.loginapplication


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.fragment_login.view.*
import mtaubert.loginapplication.databinding.FragmentLoginBinding




class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(inflater,
            R.layout.fragment_login,container,false)
        //The complete onClickListener with Navigation
        binding.signUpButton.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        binding.loginButton.setOnClickListener { view : View ->
            if(loginValidation(binding.usernameInput.text.toString(), binding.passwordInput.text.toString())) {
                view.findNavController().navigate(R.id.action_loginFragment_to_accountFragment)
            } else {
                Toast.makeText(getActivity(), "Username and/or password incorrect", Toast.LENGTH_LONG).show()
            }
        }
        return binding.root
    }

    private fun loginValidation(username:String, password:String): Boolean {
        if(username == "test" && password == "pass") {
            return true
        }
        return false
    }
}
