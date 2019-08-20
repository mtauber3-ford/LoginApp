package mtaubert.loginapplication


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.databinding.DataBindingUtil
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
            view.findNavController().navigate(R.id.action_loginFragment_to_accountFragment)
        }
        return binding.root


    }
}
