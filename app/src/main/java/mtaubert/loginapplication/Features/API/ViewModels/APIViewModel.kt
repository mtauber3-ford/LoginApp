package mtaubert.loginapplication.Features.API.ViewModels

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
import mtaubert.loginapplication.Features.API.Models.APIModel
import java.net.URLEncoder
import okhttp3.OkHttpClient
import mtaubert.loginapplication.Utils.ConnectionInterceptor


class APIViewModel(app: Application): AndroidViewModel(app) {

    private val apiModel = APIModel() //model for the login details
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
        apiModel.currentCard = null
        apiModel.currentSearchResult.clear()
        apiModel.currentSearchQuery = null
        currentPage = 0
    }

    fun getCurrentCard(): Card? {
        return apiModel.currentCard
    }

    fun setCurrentCard(card:Card) {
        apiModel.currentCard = card
    }

    fun getCurrentSearch(): ScryfallCardList? {
        if(apiModel.currentSearchResult.size > 0){
            return apiModel.currentSearchResult[currentPage]
        } else {
            return null
        }
    }

    fun getLastSearchQuery(): String? {
        return apiModel.currentSearchQuery
    }

    fun hasNextSetOfCards(): Boolean {
        return apiModel.currentSearchResult[currentPage]!!.has_more
    }

    fun hasPreviousSetOfCards(): Boolean {
        return currentPage > 0
    }

    fun getTotalNumberOfResults(): Int {
        return apiModel.currentSearchResult[currentPage]!!.total_cards
    }

    fun getCurrentPage(): Int {
        return currentPage
    }

    suspend fun getNextPageOfResults(change: Int): List<Card> {
        val nextPage = currentPage + change
        if(apiModel.currentSearchResult.size <= nextPage) {
            val scryfallList = service.getCardsByWholeURL(apiModel.currentSearchResult[currentPage]!!.next_page).await()

            //Cache results
            apiModel.currentSearchResult.add(scryfallList)
            currentPage = nextPage

            return scryfallList.data
        } else {
            currentPage = nextPage
            return apiModel.currentSearchResult[nextPage]!!.data
        }
    }

    suspend fun getRandomCard(): Card{
        apiModel.currentCard = service.getRandomCard().await()
        return apiModel.currentCard!!
    }

    suspend fun searchForCards(searchString: String, searchType: String, colorSelection: Array<Boolean>, colorSearchType: Int, formatSearch: Int): List<Card> {
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
        val scryfallList = service.getCardsByName(encodedStrings).await()

        //Cache results
        apiModel.currentSearchQuery = encodedStrings
        apiModel.currentSearchResult.add(scryfallList) //put into the adapter for continuous list

        return scryfallList.data
    }

    fun getCardImage(card: Card, imageView: ImageView) {
        DownloadCardImage(imageView).execute(card.image_uris.normal)
    }

    /**
     * CURRENT USER GETTER AND SETTER
     */
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
        GlobalScope.launch {
            apiModel.currentUserFavorites =  db.favDao().getUserFavorites(getCurrentUser()!!.email)
        }
    }

    /**
     * FAVORITES
     */
    fun getUserFavorites(): List<Card>? {
        val favoriteCardsList: ArrayList<Card> = ArrayList()
        if(!apiModel.currentUserFavorites.isNullOrEmpty()) {
            for(fav:Favorites in apiModel.currentUserFavorites!!) {
                favoriteCardsList.add(gson.fromJson(fav.cardJson, Card::class.java))
            }
        }
        currentPage = 0
        apiModel.currentSearchResult.clear()
        apiModel.currentSearchResult.add(ScryfallCardList(favoriteCardsList.size, false, "", favoriteCardsList))
        return favoriteCardsList
    }

    fun isCurrentCardAFavorite():Boolean {
        for(fav:Favorites in apiModel.currentUserFavorites!!) {
            if(apiModel.currentCard!!.id == fav.favCardId) {
                return true
            }
        }
        return false
    }

    suspend fun favoriteCurrentCard(){
        if(isCurrentCardAFavorite()) {
            db.favDao().deleteFavorite(getCurrentUser()!!.email, getCurrentCard()!!.id)
        } else {
            val newFavorite = Favorites(0, getCurrentUser()!!.email, getCurrentCard()!!.id, gson.toJson(getCurrentCard()!!))
            db.favDao().insert(newFavorite)
        }
        apiModel.currentUserFavorites =  db.favDao().getUserFavorites(getCurrentUser()!!.email)
    }

    suspend fun clearAllFavorites() {
        for(fav:Favorites in db.favDao().getUserFavorites(getCurrentUser()!!.email)) {
            db.favDao().deleteFavorite(fav.userEmail, fav.favCardId)
        }
        apiModel.currentUserFavorites =  db.favDao().getUserFavorites(getCurrentUser()!!.email)
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
