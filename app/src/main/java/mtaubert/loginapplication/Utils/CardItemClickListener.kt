package mtaubert.loginapplication.Utils

import mtaubert.loginapplication.Data.Remote.Model.Card

interface CardItemClickListener {
    fun viewCard(card: Card)
}