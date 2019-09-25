package mtaubert.loginapplication.Features.API.Views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import mtaubert.loginapplication.Data.Remote.Model.Card

import mtaubert.loginapplication.R
import mtaubert.loginapplication.Utils.Adapters.CardListAdapter
import mtaubert.loginapplication.Utils.Fragments.BaseAPIFragment
import mtaubert.loginapplication.databinding.ApiFragmentMultiCardViewBinding
import mtaubert.loginapplication.databinding.ApiFragmentOneCardViewBinding

class MultiCardViewFragment(private val cards: List<Card>) : BaseAPIFragment() {

    companion object {
        fun newInstance(cards: List<Card>): MultiCardViewFragment {
            return MultiCardViewFragment(cards)
        }
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

        showCards(binding)

        binding.landingButton.setOnClickListener {
            (activity as APIActivity).changeFragment("landing")
        }

        return binding.root
    }

    private fun showCards(binding: ApiFragmentMultiCardViewBinding) {
        val cardRecycleListView = binding.recyclerView
        val cardAdapter = CardListAdapter()
        cardRecycleListView.adapter = cardAdapter
        cardRecycleListView.layoutManager = LinearLayoutManager(context!!)
        cardAdapter.setCards(cards)

    }


}
