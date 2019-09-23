package mtaubert.loginapplication.Features.API.ViewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import mtaubert.loginapplication.Data.DB.Model.User
import mtaubert.loginapplication.Data.DB.UserRoomDatabase
import mtaubert.loginapplication.Data.Remote.DAO.GetScryfallData
import mtaubert.loginapplication.Data.Remote.Model.Card
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class APIViewModel(app: Application): AndroidViewModel(app) {

    private val apiModel = mtaubert.loginapplication.Features.API.Models.APIModel(null) //model for the login details
    private var db: UserRoomDatabase = UserRoomDatabase.getInstance(app) //Database used for user info

    private var randomCardCompositeDisposable: CompositeDisposable? = null
//    private val API_URL = "https://api.magicthegathering.io/v1/"
    private val API_URL = "https://api.scryfall.com/"

    init {
        randomCardCompositeDisposable = CompositeDisposable()
        //loadData()
    }

    fun clearDisposables() {
        randomCardCompositeDisposable?.clear()
    }

    fun loadData() = runBlocking{
        Log.e("CARD_OUTPUT", "LOADING RESULTS")

//        val requestInterface = Retrofit.Builder()
//            .baseUrl(API_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .build()
//            .create(GetScryfallData::class.java)
        val service = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(GetScryfallData::class.java)

//        try {
        val card = service.getRandomCard().await()
        Log.e("CARD", "Card: ${card.name} ${card.mana_cost}")
//        cards.forEach { Log.e("CARD", it.name) }
//        } catch (e:Exception) {
//            Log.e("CARD_OUTPUT", e.toString())
//        }

//        randomCardCompositeDisposable?.add(requestInterface.getRandomCard()
//            .observeOn(AndroidSchedulers.mainThread()).doOnError { handleError() }
//            .subscribeOn(Schedulers.io())
//            .subscribe(this::handleResponse))
    }

    fun sendGet() {
        val url = URL("https://api.scryfall.com/cards/random")

        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"  // optional default is GET

            Log.e("SCRYFALL OUTPUT","Sent 'GET' request to URL : $url; Response Code : $responseCode")
        }
    }

    private fun handleError(){
        Log.d("CARD_OUTPUT", "ERROR SHOWING RESULTS")
    }

    private fun handleResponse(cards: List<Card>){
        Log.d("CARD_OUTPUT", "SHOWING RESUlTS")

        for(card:Card in cards) {
            Log.d("CARD_OUTPUT", card.name)
        }
    }

    /**
     * Returns the current logged in user
     */
    fun getCurrentUser(): User? {
        return apiModel.currentUser
    }

    /**
     * sets the current User
     */
    fun setCurrentUser(currentUser: User) {
        apiModel.currentUser = currentUser
    }
}
