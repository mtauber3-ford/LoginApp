package mtaubert.loginapplication.Data.Remote.Model

data class Card(
    val id: String,
    val oracle_id: String,
    val name: String,
    val mana_cost: String,
    val cmc: Int,
    val type_line: String,
    val oracle_text: String,
    val power: String,
    val toughness: String,
    val colors: Array<String>,
    val color_identity: Array<String>,
    val legalities: Legalities,
    val image_uris: ImageUris,
    val set: String,
    val rarity: String,
    val artist: String

)