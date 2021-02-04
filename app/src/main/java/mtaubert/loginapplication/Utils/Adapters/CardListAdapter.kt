package mtaubert.loginapplication.Utils.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mtaubert.loginapplication.Data.Remote.Model.Card
import mtaubert.loginapplication.R
import mtaubert.loginapplication.Utils.CardItemClickListener
import mtaubert.loginapplication.databinding.ApiFragmentOneCardViewBinding


class CardListAdapter(val cardClickListner: (Card) -> Unit) : RecyclerView.Adapter<CardListAdapter.CardViewHolder>() {

    private var cardList = mutableListOf<Card>() //List of current cards

    /**
     * UserViewHolder
     * Class for the user info view for the recylcer view
     */
    inner class CardViewHolder(val cardView: View, val context:Context) : RecyclerView.ViewHolder(cardView) {
        val tvName: TextView = cardView.findViewById(R.id.name_textview)
        val tvCMC: TextView = cardView.findViewById(R.id.cmc_textview)
        val tvType: TextView = cardView.findViewById(R.id.type_line_textview)
        val tvNumber: TextView = cardView.findViewById(R.id.item_number)
        val costLayout: LinearLayout = cardView.findViewById(R.id.cost_layout)

        fun bind(card:Card, cardClickListner: (Card) -> Unit) {
            cardView.setOnClickListener{
                cardClickListner(card)
            }
        }
    }

    /**
     * Creates and returns a view holder for the list
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val cardView = inflater.inflate(R.layout.utils_card_entry_item, parent, false)
        return CardViewHolder(cardView, parent.context)
    }

    /**
     * Returns the number of items in the list
     */
    override fun getItemCount(): Int {
        return cardList.size
    }

    /**
     * Adds data from the database to the viewholder
     */
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val current = cardList[position]
        holder.tvName.text = current.name
        holder.tvCMC.text = current.cmc.toString()
        holder.tvType.text = current.type_line
        var colorIdentity = ""
        if (current.color_identity != null) {
            for (color in current.color_identity) {
                colorIdentity += color
            }
        }
        holder.tvNumber.text = colorIdentity
        holder.costLayout.removeAllViews()
        renderManaCost(current.mana_cost, holder.costLayout, holder.context)
        holder.bind(current, cardClickListner)
    }

    private fun renderManaCost(cost: String, costLayout: LinearLayout, context: Context) {
        var manaCost = cost
        if(manaCost != null) {
            while(manaCost.isNotEmpty()) {
                val newSymbol = ImageView(context)
                costLayout.addView(newSymbol)
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

    /**
     * Sets the list of users
     */
    internal fun setCards(cards: List<Card>) {
        this.cardList.addAll(cards)
        notifyDataSetChanged()
    }

    internal fun addCards(cards: List<Card>) {
        this.cardList.addAll(cards)
        notifyDataSetChanged()
    }
}

