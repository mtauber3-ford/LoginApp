package mtaubert.loginapplication.Data.Remote.DAO

import mtaubert.loginapplication.Data.Remote.Model.Card
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import mtaubert.loginapplication.Data.Remote.Model.ScryfallCardList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetScryfallData {

    @GET("cards/random")
    fun getRandomCard(): Deferred<Card>

    @GET("cards/search?")
    fun getCardsNames(@Query("q") q: String): Deferred<ScryfallCardList>
//    fun getCardsNames(): Deferred<List<Card>>

//    @GET("users/{user}/repos")
//    fun listRepos(@Path("user") user: String): Call<List<Repo>>
}