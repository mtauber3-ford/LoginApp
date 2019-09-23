package mtaubert.loginapplication.Features.API.Views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mtaubert.loginapplication.Features.Login.ViewModels.LoginViewModel
import mtaubert.loginapplication.Features.Login.Views.LoginActivity
import mtaubert.loginapplication.R
import mtaubert.loginapplication.Utils.Fragments.BaseAPIFragment
import mtaubert.loginapplication.Utils.Fragments.BaseLoginFragment
import mtaubert.loginapplication.databinding.ApiFragmentLandingBinding
import mtaubert.loginapplication.databinding.LoginFragmentLoginBinding
import java.lang.Exception

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

        binding.homeButton.setOnClickListener {
            val intent = Intent((activity as APIActivity), LoginActivity::class.java)
            intent.putExtra("currentUser", apiViewModel.getCurrentUser())
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            GlobalScope.launch {
                val cards = listOf(apiViewModel.getRandomCard())
                (activity as APIActivity).showCards(cards)
            }
        }

        return binding.root
    }
}
