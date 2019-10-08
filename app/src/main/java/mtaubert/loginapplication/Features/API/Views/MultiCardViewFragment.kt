package mtaubert.loginapplication.Features.API.Views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import mtaubert.loginapplication.Data.Remote.Model.Card

import mtaubert.loginapplication.R
import mtaubert.loginapplication.Utils.Adapters.CardListAdapter
import mtaubert.loginapplication.Utils.CardItemClickListener
import mtaubert.loginapplication.Utils.Fragments.BaseAPIFragment
import mtaubert.loginapplication.databinding.ApiFragmentMultiCardViewBinding
import mtaubert.loginapplication.databinding.ApiFragmentOneCardViewBinding

class MultiCardViewFragment(private val cards: List<Card>) : BaseAPIFragment(), CardItemClickListener {

    companion object {
        fun newInstance(cards: List<Card>): MultiCardViewFragment {
            return MultiCardViewFragment(cards)
        }
    }

    override fun viewCard(card: Card) {
        (activity as APIActivity).showCards(listOf(card))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<ApiFragmentMultiCardViewBinding>(
            inflater,
            R.layout.api_fragment_multi_card_view,
            container,
            false
        )

        binding.tvCardTotal.text = apiViewModel.getTotalNumberOfResults().toString()

        if(!apiViewModel.hasNextSetOfCards()) {
            binding.nextButton.visibility = View.INVISIBLE
        }

        if(!apiViewModel.hasPreviousSetOfCards()) {
            binding.backButton.visibility = View.INVISIBLE
        }

        if(cards.isNullOrEmpty()) {
            Toast.makeText(context, "No cards found!", Toast.LENGTH_LONG).show()
        }

        showCards(binding)

        binding.landingButton.setOnClickListener {
            (activity as APIActivity).changeFragment("landing")
        }

        return binding.root
    }

    private fun showCards(binding: ApiFragmentMultiCardViewBinding) {
        val cardRecycleListView = binding.recyclerView
        val cardAdapter = CardListAdapter { card:Card -> viewCard(card)}
        cardRecycleListView.adapter = cardAdapter
        cardRecycleListView.layoutManager = LinearLayoutManager(context!!)
        cardAdapter.setCards(cards)
    }


}
