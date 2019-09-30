package mtaubert.loginapplication.Features.API.Views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.api_fragment_one_card_view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

    private var favorite: Boolean = false

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

        favorite = apiViewModel.isCurrentCardAFavorite()

        if(favorite) {
            binding.imageButton.setImageResource(R.drawable.heart_2)
            favorite = !favorite
        }

        binding.backToLandingButton.setOnClickListener {
            (activity as APIActivity).contextualBackButtonPressed()
        }

        binding.imageButton.setOnClickListener {
            GlobalScope.launch { apiViewModel.favoriteCurrentCard() }
            setHeartIcon(binding)
        }

        showCard(binding)

        return binding.root
    }

    private fun setHeartIcon(binding: ApiFragmentOneCardViewBinding) {
        if(favorite) {
            binding.imageButton.setImageResource(R.drawable.heart)
        } else {
            binding.imageButton.setImageResource(R.drawable.heart_2)
        }
        favorite = !favorite
    }

    private fun showCard(binding: ApiFragmentOneCardViewBinding) {
        binding.nameTextview.text = card.name
        binding.cmcTextview.text = "${card.mana_cost} (${card.cmc})"
        binding.typelineTextview.text = card.type_line
        binding.oracleTextTextview.text = card.oracle_text
        apiViewModel.getCardImage(card, binding.imageView)
    }
}
