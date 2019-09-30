package mtaubert.loginapplication.Features.API.Models

import mtaubert.loginapplication.Data.DB.Model.Favorites
import mtaubert.loginapplication.Data.DB.Model.User
import mtaubert.loginapplication.Data.Remote.Model.Card
import mtaubert.loginapplication.Data.Remote.Model.ScryfallCardList

data class APIModel(
    var currentUser: User? = null,
    var currentUserFavorites: List<Favorites>? = null,
    var lastCardSearchResult: ScryfallCardList? = null,
    var lastScryfallSearchQuery: String? = null,
    var lastCardInspected: Card? = null
)