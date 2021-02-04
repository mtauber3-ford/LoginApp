package mtaubert.loginapplication.Features.API.Views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.api_fragment_multi_card_view.*
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

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        tv_card_total.text = apiViewModel.getTotalNumberOfResults().toString()

        if(cards.isNullOrEmpty()) {
            Toast.makeText(context, "No cards found!", Toast.LENGTH_LONG).show()
        }

        landing_button.setOnClickListener {
            (activity as APIActivity).changeFragment("landing") //communicate with activity and change the name at the top
        }

//        showButtons()
        showCards()
    }

//    private fun showButtons() {
//        next_button.isEnabled = true
//        back_button.isEnabled = true
//        if(!apiViewModel.hasNextSetOfCards()) {
//            next_button.visibility = View.INVISIBLE
//        } else {
//            next_button.visibility = View.VISIBLE
//        }
//
//        if(!apiViewModel.hasPreviousSetOfCards()) {
//            back_button.visibility = View.INVISIBLE
//        } else {
//            back_button.visibility = View.VISIBLE
//        }
//    }

    private fun nextPage() = GlobalScope.launch { //data binding
//        val newCards = apiViewModel.getNextPageOfResults(change)
//        (activity as APIActivity).runOnUiThread {
//            cards = newCards!!
//            showButtons()
//            showCards()
////            Toast.makeText(context, apiViewModel.getCurrentPage().toString(), Toast.LENGTH_LONG).show()
//        }
        val newCards = apiViewModel.getNextPageOfResults()
        (activity as APIActivity).runOnUiThread {
            if (newCards != null) {
                addCards(newCards)
            } else {
                Toast.makeText(context, "End of results!", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun showCards() {
        val cardRecycleListView = recyclerView
        val cardAdapter = CardListAdapter { card:Card -> viewCard(card)}
        cardRecycleListView.adapter = cardAdapter
        cardRecycleListView.layoutManager = LinearLayoutManager(context!!)
        cardAdapter.addCards(cards)
        nextPage()
    }

    private fun addCards(cards:List<Card>) {
        (recyclerView.adapter as CardListAdapter).addCards(cards)
    }


}
