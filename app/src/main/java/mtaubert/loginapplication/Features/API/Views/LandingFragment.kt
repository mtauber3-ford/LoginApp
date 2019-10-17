package mtaubert.loginapplication.Features.API.Views

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.api_fragment_landing.*
import kotlinx.android.synthetic.main.api_fragment_one_card_view.*
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

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupButtons()
    }

    private fun setupButtons() {
        home_button.setOnClickListener {
            val intent = Intent((activity as APIActivity), LoginActivity::class.java)
            intent.putExtra("currentUser", apiViewModel.getCurrentUser())
            startActivity(intent)
        }

        favorites_button.setOnClickListener {
            apiViewModel.clearSearchCache()
            GlobalScope.launch {
                val cards = apiViewModel.getUserFavorites()
                (activity as APIActivity).showCards(cards!!)
            }
        }

        clear_favorites_button.setOnClickListener {
            GlobalScope.launch {
                apiViewModel.clearAllFavorites()
            }
        }

        button.setOnClickListener {
            GlobalScope.launch {
                val cards = apiViewModel.getRandomCard()
                (activity as APIActivity).showCards(cards)
            }
        }

        search_button.setOnClickListener {
            val searchString = search_edit_text.text.toString()
            val searchType = card_type_spinner.selectedItem.toString()
            val colorSelection = arrayOf(
                w_checkbox.isChecked,
                u_checkbox.isChecked,
                b_checkbox.isChecked,
                r_checkbox.isChecked,
                g_checkbox.isChecked,
                c_checkbox.isChecked
            )
            val colorSearchType = color_option_spinner.selectedItemPosition
            val formatSearch = format_option_spinner.selectedItemPosition

            if(searchString.isBlank() && searchString.isEmpty() && searchType == "Any" && !colorSelection.contains(true) && formatSearch == 0) {
                Toast.makeText(activity, "Please enter something to search", Toast.LENGTH_LONG).show()
            } else {
                GlobalScope.launch {
                    val cards = apiViewModel.searchForCards(
                       searchString,
                        searchType,
                        colorSelection,
                        colorSearchType,
                        formatSearch
                    )
                    (activity as APIActivity).showCards(cards)
                }
            }
        }
    }
}
