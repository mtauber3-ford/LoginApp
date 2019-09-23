package mtaubert.loginapplication.Data.Remote.Model

data class Card(
    val id: String,
    val oracle_id: String,
    val name: String,
    val mana_cost: String,
    val cmc: Int,
    val type_line: String,
    val oracle_text: String,
//    val power: Int,
//    val toughness: Int,
//    val colors: ArrayList<String>,
//    val color_identity: ArrayList<String>,
    val legalities: Legalities,
    val set: String,
    val rarity: String,
    val artist: String

)