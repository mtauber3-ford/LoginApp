package mtaubert.loginapplication.Data.Remote.DAO

import mtaubert.loginapplication.Data.Remote.Model.Card
import kotlinx.coroutines.Deferred
import mtaubert.loginapplication.Data.Remote.Model.ScryfallCardList
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface GetScryfallData {

    @GET("cards/random")
    fun getRandomCard(): Deferred<Card>

    @GET("cards/search")
    fun getCardsByName(@Query("q", encoded=true) query: String): Deferred<ScryfallCardList>

    @GET
    fun getCardsByWholeURL(@Url url: String): Deferred<ScryfallCardList>

}