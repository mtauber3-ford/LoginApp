package mtaubert.loginapplication.Data.Remote.Model

data class Card(
    val scryfallID: String,
    val oracleID: String,
    val name: String,
    val manaCost: String,
    val cmc: Int,
    val typeLine: String,
    val oracleText: String,
    val power: Int,
    val toughness: Int,
    val colors: ArrayList<String>,
    val colorIdentity: ArrayList<String>,
    val legalities: ArrayList<String>,
    val set: String,
    val rarity: String,
    val artist: String

)