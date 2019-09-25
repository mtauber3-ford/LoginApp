package mtaubert.loginapplication.Features.API.Views

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mtaubert.loginapplication.Features.Login.Views.LoginActivity
import mtaubert.loginapplication.R
import mtaubert.loginapplication.Utils.Fragments.BaseAPIFragment
import mtaubert.loginapplication.databinding.ApiFragmentLandingBinding
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

        setupButtons(binding)

        return binding.root
    }

    fun setupButtons(binding: ApiFragmentLandingBinding) {
        binding.homeButton.setOnClickListener {
            val intent = Intent((activity as APIActivity), LoginActivity::class.java)
            intent.putExtra("currentUser", apiViewModel.getCurrentUser())
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            try {
                GlobalScope.launch {
                    val cards = listOf(apiViewModel.getRandomCard())
                    (activity as APIActivity).showCards(cards)
                }
            } catch (e: Exception) {
                Toast.makeText(activity, "Error loading cards", Toast.LENGTH_LONG).show()
            }
        }

        binding.searchButton.setOnClickListener {
            val searchString = binding.searchEditText.text.toString()
            val searchType = binding.cardTypeSpinner.selectedItem.toString()
            val colorSelection = arrayOf(
                binding.wCheckbox.isChecked,
                binding.uCheckbox.isChecked,
                binding.bCheckbox.isChecked,
                binding.rCheckbox.isChecked,
                binding.gCheckbox.isChecked,
                binding.cCheckbox.isChecked
            )
            val colorSearchType = binding.spinner.selectedItemPosition

            if(searchString.isBlank() && searchString.isEmpty() && searchType == "Any" && !colorSelection.contains(true)) {
                Toast.makeText(activity, "Please enter something to search", Toast.LENGTH_LONG).show()
            } else {
                try {
                    GlobalScope.launch {
                        val cards = apiViewModel.searchForCards(
                           searchString,
                            searchType,
                            colorSelection,
                            colorSearchType
                        )
                        (activity as APIActivity).showCards(cards)
                    }
                } catch (e:Exception) {
                    Toast.makeText(activity, "Error loading cards", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
