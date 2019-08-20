package mtaubert.loginapplication


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import mtaubert.loginapplication.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAccountBinding>(inflater,
            R.layout.fragment_account,container,false)
        //The complete onClickListener with Navigation
        return binding.root
    }
}
