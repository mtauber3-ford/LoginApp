package mtaubert.loginapplication.Features.API.ViewModels

import NoNetworkConnectivityException
import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.AndroidViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import mtaubert.loginapplication.Data.DB.Model.User
import mtaubert.loginapplication.Data.DB.UserRoomDatabase
import mtaubert.loginapplication.Data.Remote.DAO.GetScryfallData
import mtaubert.loginapplication.Data.Remote.Model.Card
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mtaubert.loginapplication.Data.DB.Model.Favorites
import mtaubert.loginapplication.Data.Remote.Model.ScryfallCardList
import java.net.URLEncoder
import okhttp3.OkHttpClient
import mtaubert.loginapplication.Utils.ConnectionInterceptor
import mtaubert.loginapplication.Utils.InvalidSearchException
import java.net.UnknownHostException
import kotlin.collections.ArrayList


class APIViewModel(app: Application): AndroidViewModel(app) {
    private var currentErrorMessage: String? = null
    private var currentUser: User? = null
    private var currentUserFavorites: List<Favorites>? = null
    private var currentSearchResult: MutableList<ScryfallCardList?> = mutableListOf()
    private var currentSearchQuery: String? = null
    private var currentCard: Card? = null

    private var db: UserRoomDatabase = UserRoomDatabase.getInstance(app) //Database used for user info

    private var service: GetScryfallData
    private val gson: Gson = Gson()

    private val API_URL = "https://api.scryfall.com/"

    private var currentPage = 0

    init {
        val oktHttpClient = OkHttpClient.Builder()
            .addInterceptor(ConnectionInterceptor(app.applicationContext))

        service = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(oktHttpClient.build())
            .build()
            .create(GetScryfallData::class.java)
    }

    /**
     * CARD SEARCHES
     */
    fun clearSearchCache()
    {
        currentCard = null
        currentSearchResult.clear()
        currentSearchQuery = null
        currentPage = 0
    }

    fun getCurrentErrorMessage(): String {
        if (currentErrorMessage != null) {
            return currentErrorMessage!!
        }
        return "Error occured getting results"
    }

    fun getCurrentCard(): Card? {
        return currentCard
    }

    fun setCurrentCard(card:Card) {
        currentCard = card
    }

    fun getCurrentSearch(): ScryfallCardList? {
        if(currentSearchResult.size > 0){
            return currentSearchResult[currentPage]
        } else {
            return null
        }
    }

    fun getLastSearchQuery(): String? {
        return currentSearchQuery
    }

    fun hasNextSetOfCards(): Boolean {
        return currentSearchResult[currentPage]!!.has_more
    }

    fun hasPreviousSetOfCards(): Boolean {
        return currentPage > 0
    }

    fun getTotalNumberOfResults(): Int {
        return currentSearchResult[currentPage]!!.total_cards
    }

    fun getCurrentPage(): Int {
        return currentPage
    }

    private suspend fun makeScryfallAPICall(type: String, query: String?): List<Card>? {
        return try {
            when(type) {
                "random" -> {
                    currentCard = service.getRandomCard().await()
                    listOf(currentCard!!)
                }
                "userQuery" -> {
                    val scryfallList = service.getCardsByName(query!!).await()
                    currentSearchResult.add(scryfallList) //put into the adapter for continuous list
                    scryfallList.data
                }
                "url" -> {
                    val scryfallList = service.getCardsByWholeURL(query!!).await()
                    currentSearchResult.add(scryfallList)
                    scryfallList.data
                }
                else -> listOf()
            }
        } catch (e: Exception) {
            currentErrorMessage = e.message
            when (e) {
                is NoNetworkConnectivityException -> null
                is UnknownHostException -> null
                is InvalidSearchException -> listOf()
                else -> null
            }
        }
    }

    suspend fun getNextPageOfResults(): List<Card>? {
        if(currentSearchResult[currentPage]!!.has_more) {
            val query = currentSearchResult[currentPage]!!.next_page
            currentPage++
            return makeScryfallAPICall("url", query)
        } else {
            return null
        }
    }

    suspend fun getRandomCard(): List<Card>?{
        clearSearchCache()
        return makeScryfallAPICall("random", null)

    }

    suspend fun searchForCards(searchString: String, searchType: String, colorSelection: Array<Boolean>, colorSearchType: Int, formatSearch: Int): List<Card>? {
        var encodedStrings = ""
        if(searchString.isNotEmpty() && searchString.isNotBlank()) {
            encodedStrings += URLEncoder.encode(searchString, "UTF-8")
        }

        if(searchType != "Any") {
            if(encodedStrings.isNotEmpty()) {
                encodedStrings += "+"
            }
            encodedStrings += "type:" + URLEncoder.encode(searchType, "UTF-8")
        }

        if(colorSelection.contains(true)) {
            if(encodedStrings.isNotEmpty()) {
                encodedStrings += "+"
            }

            val colors = arrayOf("W", "U", "B", "R", "G", "C")
            var colorString = ""
            for(i in colorSelection.indices) {
                if(colorSelection[i]) {
                    colorString += colors[i]
                }
            }

            when(colorSearchType) {
                0 -> { //exactly
                    encodedStrings += "color=$colorString"
                }
                1 -> { //including
                    encodedStrings += "color>=$colorString"
                }
                2 -> { //at most
                    encodedStrings += "color<=$colorString"
                }
            }
        }

        if(formatSearch != 0) {
            if(encodedStrings.isNotEmpty()) {
                encodedStrings += "+"
            }
            val formats = arrayOf(
                "standard","future", "modern", "legacy", "pauper", "vintage", "penny", "commander", "brawl", "duel", "oldschool"
            )

            encodedStrings += "legal:${formats[formatSearch-1]}"
        }

        Log.e("SEARCH STRING", encodedStrings)

        clearSearchCache()
        currentSearchQuery = encodedStrings
        return makeScryfallAPICall("userQuery", encodedStrings)
    }

    fun getCardImage(card: Card, imageView: ImageView) = GlobalScope.launch {
        DownloadCardImage(imageView).execute(card.image_uris.normal)
    }

    /**
     * CURRENT USER GETTER AND SETTER
     */
    /**
     * Returns the current logged in user
     */
    fun getCurrentUser(): User? {
        return currentUser
    }

    /**
     * sets the current User
     */
    fun setCurrentUser(user: User) {
        currentUser = user
        GlobalScope.launch {
            currentUserFavorites =  db.favDao().getUserFavorites(getCurrentUser()!!.email)
        }
    }

    /**
     * FAVORITES
     */
    fun getUserFavorites(): List<Card>? {
        val favoriteCardsList: ArrayList<Card> = ArrayList()
        if(!currentUserFavorites.isNullOrEmpty()) {
            for(fav:Favorites in currentUserFavorites!!) {
                favoriteCardsList.add(gson.fromJson(fav.cardJson, Card::class.java))
            }
        }
        clearSearchCache()
        currentSearchResult.add(ScryfallCardList(favoriteCardsList.size, false, "", favoriteCardsList))
        return favoriteCardsList
    }

    fun isCurrentCardAFavorite():Boolean {
        for(fav:Favorites in currentUserFavorites!!) {
            if(currentCard!!.id == fav.favCardId) {
                return true
            }
        }
        return false
    }

    fun getManaCost() {

    }

    suspend fun favoriteCurrentCard(){
        if(isCurrentCardAFavorite()) {
            db.favDao().deleteFavorite(getCurrentUser()!!.email, getCurrentCard()!!.id)
        } else {
            val newFavorite = Favorites(0, getCurrentUser()!!.email, getCurrentCard()!!.id, gson.toJson(getCurrentCard()!!))
            db.favDao().insert(newFavorite)
        }
        currentUserFavorites =  db.favDao().getUserFavorites(getCurrentUser()!!.email)
    }

    suspend fun clearAllFavorites() {
        for(fav:Favorites in db.favDao().getUserFavorites(getCurrentUser()!!.email)) {
            db.favDao().deleteFavorite(fav.userEmail, fav.favCardId)
        }
        currentUserFavorites =  db.favDao().getUserFavorites(getCurrentUser()!!.email)
    }

    private inner class DownloadCardImage(internal var imageView: ImageView) :
        AsyncTask<String, Void, Bitmap>() {

        override fun doInBackground(vararg urls: String): Bitmap? {
            val imageURL = urls[0]
            var bimage: Bitmap? = null
            try {
                val urlIN = java.net.URL(imageURL).openStream()
                bimage = BitmapFactory.decodeStream(urlIN)

            } catch (e: Exception) {
                Log.e("Error Message", e.message)
                e.printStackTrace()
            }

            return bimage
        }

        override fun onPostExecute(result: Bitmap) {
            imageView.setImageBitmap(result)
        }
    }
}
