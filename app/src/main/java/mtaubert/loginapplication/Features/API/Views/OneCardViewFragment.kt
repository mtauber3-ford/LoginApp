package mtaubert.loginapplication.Features.API.Views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.api_fragment_one_card_view.*
import kotlinx.android.synthetic.main.login_fragment_dashboard.*
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

        return binding.root
    }

    override fun onStart() {
        if(favorite) {
            imageButton.setImageResource(R.drawable.heart_2)
        }

        back_to_landing_button.setOnClickListener {
            (activity as APIActivity).contextualBackButtonPressed()
        }

        super.onStart()
        imageButton.setOnClickListener {
            GlobalScope.launch { apiViewModel.favoriteCurrentCard() }
            setHeartIcon()
        }

        showCard()
    }

    private fun setHeartIcon() {
        if(favorite) {
            imageButton.setImageResource(R.drawable.heart)
        } else {
            imageButton.setImageResource(R.drawable.heart_2)
        }
        favorite = !favorite
    }

    private fun showCard() {
        name_textview.text = card.name
        cmc_textview.text = "(${card.cmc})"
        typeline_textview.text = card.type_line
        oracle_text_textview.text = card.oracle_text
        apiViewModel.getCardImage(card, imageView)

        //binding.manaCostDisplay.renderManaCost(card.mana_cost)
        renderManaCost()
    }

    private fun renderManaCost() {
        var manaCost = card.mana_cost
        while(manaCost.isNotEmpty()) {
            val newSymbol = ImageView(context)
            manaSymbolLayout.addView(newSymbol)
            newSymbol.layoutParams.height = 64
            newSymbol.layoutParams.width = 64
            newSymbol.scaleType = ImageView.ScaleType.FIT_CENTER

            val symbolSubstring = manaCost.substring(
                manaCost.indexOf("{"),
                manaCost.indexOf("}")+1
            )

            when(symbolSubstring) {
                "{W}" -> newSymbol.setImageResource(R.drawable.ic_w)
                "{U}" -> newSymbol.setImageResource(R.drawable.ic_u)
                "{B}" -> newSymbol.setImageResource(R.drawable.ic_b)
                "{R}" -> newSymbol.setImageResource(R.drawable.ic_r)
                "{G}" -> newSymbol.setImageResource(R.drawable.ic_g)
                "{C}" -> newSymbol.setImageResource(R.drawable.ic_c)
                "{0}" -> newSymbol.setImageResource(R.drawable.ic_0)
                "{1}" -> newSymbol.setImageResource(R.drawable.ic_1)
                "{2}" -> newSymbol.setImageResource(R.drawable.ic_2)
                "{3}" -> newSymbol.setImageResource(R.drawable.ic_3)
                "{4}" -> newSymbol.setImageResource(R.drawable.ic_4)
                "{5}" -> newSymbol.setImageResource(R.drawable.ic_5)
                "{6}" -> newSymbol.setImageResource(R.drawable.ic_6)
                "{7}" -> newSymbol.setImageResource(R.drawable.ic_7)
                "{8}" -> newSymbol.setImageResource(R.drawable.ic_8)
                "{9}" -> newSymbol.setImageResource(R.drawable.ic_9)
                "{10}" -> newSymbol.setImageResource(R.drawable.ic_10)
                "{11}" -> newSymbol.setImageResource(R.drawable.ic_11)
                "{12}" -> newSymbol.setImageResource(R.drawable.ic_12)
                "{13}" -> newSymbol.setImageResource(R.drawable.ic_13)
                "{14}" -> newSymbol.setImageResource(R.drawable.ic_14)
                "{15}" -> newSymbol.setImageResource(R.drawable.ic_15)
                "{2/W}" -> newSymbol.setImageResource(R.drawable.ic_2w)
                "{2/U}" -> newSymbol.setImageResource(R.drawable.ic_2u)
                "{2/B}" -> newSymbol.setImageResource(R.drawable.ic_2b)
                "{2/R}" -> newSymbol.setImageResource(R.drawable.ic_2r)
                "{2/G}" -> newSymbol.setImageResource(R.drawable.ic_2g)
                "{W/U}" -> newSymbol.setImageResource(R.drawable.ic_wu)
                "{W/B}" -> newSymbol.setImageResource(R.drawable.ic_wb)
                "{B/R}" -> newSymbol.setImageResource(R.drawable.ic_br)
                "{B/G}" -> newSymbol.setImageResource(R.drawable.ic_bg)
                "{U/B}" -> newSymbol.setImageResource(R.drawable.ic_ub)
                "{U/R}" -> newSymbol.setImageResource(R.drawable.ic_ur)
                "{R/G}" -> newSymbol.setImageResource(R.drawable.ic_rg)
                "{R/W}" -> newSymbol.setImageResource(R.drawable.ic_rw)
                "{G/W}" -> newSymbol.setImageResource(R.drawable.ic_gw)
                "{G/U}" -> newSymbol.setImageResource(R.drawable.ic_gu)
                "{W/P}" -> newSymbol.setImageResource(R.drawable.ic_wp)
                "{U/P}" -> newSymbol.setImageResource(R.drawable.ic_up)
                "{B/P}" -> newSymbol.setImageResource(R.drawable.ic_bp)
                "{R/P}" -> newSymbol.setImageResource(R.drawable.ic_rp)
                "{G/P}" -> newSymbol.setImageResource(R.drawable.ic_gp)
                "{S}" -> newSymbol.setImageResource(R.drawable.ic_s)
                "{X}" -> newSymbol.setImageResource(R.drawable.ic_x)
                 else -> newSymbol.setImageResource(R.drawable.ic_c)
            }

            manaCost = manaCost.substring(manaCost.indexOf("}")+1)
        }
    }
}
