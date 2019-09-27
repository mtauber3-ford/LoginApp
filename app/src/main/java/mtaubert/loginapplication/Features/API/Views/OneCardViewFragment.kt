package mtaubert.loginapplication.Features.API.Views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import mtaubert.loginapplication.Data.Remote.Model.Card

import mtaubert.loginapplication.R
import mtaubert.loginapplication.Utils.Fragments.BaseAPIFragment
import mtaubert.loginapplication.databinding.ApiFragmentOneCardViewBinding


/**
 * A simple [Fragment] subclass.
 */
class OneCardViewFragment(val card: Card) : BaseAPIFragment() {

    companion object {
        fun newInstance(card: Card): OneCardViewFragment {
            return OneCardViewFragment(card)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<ApiFragmentOneCardViewBinding>(
            inflater,
            R.layout.api_fragment_one_card_view,
            container,
            false
        )

        binding.backToLandingButton.setOnClickListener {
            (activity as APIActivity).contextualBackButtonPressed()
        }

        showCard(binding)

        return binding.root
    }

    private fun showCard(binding: ApiFragmentOneCardViewBinding) {
        binding.nameTextview.text = card.name
        binding.cmcTextview.text = "${card.mana_cost} (${card.cmc})"
        binding.typelineTextview.text = card.type_line
        binding.oracleTextTextview.text = card.oracle_text
        apiViewModel.getCardImage(card, binding.imageView)
    }
}
