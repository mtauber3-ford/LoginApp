package mtaubert.loginapplication.Features.API.Views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mtaubert.loginapplication.Data.Remote.Model.Card

import mtaubert.loginapplication.R
import mtaubert.loginapplication.Utils.Adapters.CardListAdapter
import mtaubert.loginapplication.Utils.CardItemClickListener
import mtaubert.loginapplication.Utils.Fragments.BaseAPIFragment
import mtaubert.loginapplication.databinding.ApiFragmentMultiCardViewBinding
import mtaubert.loginapplication.databinding.ApiFragmentOneCardViewBinding

class MultiCardViewFragment(private var cards: List<Card>) : BaseAPIFragment(), CardItemClickListener {

    lateinit var binding:ApiFragmentMultiCardViewBinding

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
            binding = DataBindingUtil.inflate<ApiFragmentMultiCardViewBinding>(
            inflater,
            R.layout.api_fragment_multi_card_view,
            container,
            false
        )

        binding.tvCardTotal.text = apiViewModel.getTotalNumberOfResults().toString()

        binding.nextButton.setOnClickListener {
            it.isEnabled = false
            changePage(1)
        }

        binding.backButton.setOnClickListener {
            it.isEnabled = false
            changePage(-1)
        }

        if(cards.isNullOrEmpty()) {
            Toast.makeText(context, "No cards found!", Toast.LENGTH_LONG).show()
        }

        showButtons()
        showCards()

        binding.landingButton.setOnClickListener {
            (activity as APIActivity).changeFragment("landing") //communicate with activity and change the name at the top
        }

        return binding.root
    }

    private fun showButtons() {
        binding.nextButton.isEnabled = true
        binding.backButton.isEnabled = true
        if(!apiViewModel.hasNextSetOfCards()) {
            binding.nextButton.visibility = View.INVISIBLE
        } else {
            binding.nextButton.visibility = View.VISIBLE
        }

        if(!apiViewModel.hasPreviousSetOfCards()) {
            binding.backButton.visibility = View.INVISIBLE
        } else {
            binding.backButton.visibility = View.VISIBLE
        }
    }

    private fun changePage(change: Int) = GlobalScope.launch { //data binding
        val newCards = apiViewModel.getNextPageOfResults(change)
        (activity as APIActivity).runOnUiThread {
            cards = newCards
            showButtons()
            showCards()
//            Toast.makeText(context, apiViewModel.getCurrentPage().toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun showCards() {
        val cardRecycleListView = binding.recyclerView
        val cardAdapter = CardListAdapter { card:Card -> viewCard(card)}
        cardRecycleListView.adapter = cardAdapter
        cardRecycleListView.layoutManager = LinearLayoutManager(context!!)
        cardAdapter.setCards(cards)
    }


}
