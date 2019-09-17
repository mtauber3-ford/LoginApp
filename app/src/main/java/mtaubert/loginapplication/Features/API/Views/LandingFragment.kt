package mtaubert.loginapplication.Features.API.Views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import mtaubert.loginapplication.Features.Login.ViewModels.LoginViewModel
import mtaubert.loginapplication.R
import mtaubert.loginapplication.Utils.Fragments.BaseAPIFragment
import mtaubert.loginapplication.Utils.Fragments.BaseLoginFragment
import mtaubert.loginapplication.databinding.ApiFragmentLandingBinding
import mtaubert.loginapplication.databinding.LoginFragmentLoginBinding

class LandingFragment : BaseAPIFragment() {

    companion object {
        fun newInstance(): LandingFragment {
            return LandingFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<ApiFragmentLandingBinding>(
            inflater,
            R.layout.api_fragment_landing,
            container,
            false
        )

        binding.userInfoTextview.append(" " + apiViewModel.getCurrentUser()?.name)
            //.append(" " + loginViewModel.getCurrentUser()!!.name)
        return binding.root
    }
}
