package mtaubert.loginapplication.Data.Remote.DAO

import mtaubert.loginapplication.Data.Remote.Model.Card
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface GetScryfallData {

//    @GET("/cards/random")
    @GET("cards/random")
    fun getRandomCard(): Deferred<Card>
}