package mtaubert.loginapplication.Data.Remote.Model

data class ScryfallCardList(
    val total_cards: Int,
    val has_more: Boolean,
    val next_page: String,
    val data: List<Card>?

)