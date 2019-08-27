package mtaubert.loginapplication


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import mtaubert.loginapplication.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAccountBinding>(inflater,
            R.layout.fragment_account,container,false)
        //The complete onClickListener with Navigation

        val user = (activity as LoginActivity).currentUser
        binding.nameDisplay.text = user?.name
        binding.emailDisplay.text = user?.email
        binding.passwordDisplay.text = user?.password

        binding.logoutButton.setOnClickListener { view : View ->
            (activity as LoginActivity).currentUser = null
            view.findNavController().navigate(R.id.action_accountFragment_to_loginFragment)
        }

        return binding.root
    }
}
