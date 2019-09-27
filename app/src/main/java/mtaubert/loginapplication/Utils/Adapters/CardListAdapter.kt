package mtaubert.loginapplication.Utils.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mtaubert.loginapplication.Data.Remote.Model.Card
import mtaubert.loginapplication.R
import mtaubert.loginapplication.Utils.CardItemClickListener


class CardListAdapter(val cardClickListner: (Card) -> Unit) : RecyclerView.Adapter<CardListAdapter.CardViewHolder>() {

    private var cardList = emptyList<Card>() //List of current cards

    /**
     * UserViewHolder
     * Class for the user info view for the recylcer view
     */
    inner class CardViewHolder(val cardView: View) : RecyclerView.ViewHolder(cardView) {
        val tvName: TextView = cardView.findViewById(R.id.name_textview)
        val tvCMC: TextView = cardView.findViewById(R.id.cmc_textview)
        val tvType: TextView = cardView.findViewById(R.id.type_line_textview)
        val tvNumber: TextView = cardView.findViewById(R.id.item_number)

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
        return CardViewHolder(cardView)
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
        holder.tvCMC.text = "${current.mana_cost} ${current.cmc}"
        holder.tvType.text = current.type_line
        holder.tvNumber.text = (position + 1).toString()

        holder.bind(current, cardClickListner)
    }

    /**
     * Sets the list of users
     */
    internal fun setCards(cards: List<Card>) {
        this.cardList = cards
        notifyDataSetChanged()
    }
}

