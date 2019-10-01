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


class APIViewModel(app: Application): AndroidViewModel(app) {

    private val apiModel = APIModel() //model for the login details
    private var db: UserRoomDatabase = UserRoomDatabase.getInstance(app) //Database used for user info

    private var service: GetScryfallData
    private val gson: Gson = Gson()

    private val API_URL = "https://api.scryfall.com/"

    init {
        service = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(GetScryfallData::class.java)

    }

    fun clearSearchCache()
    {
        apiModel.lastCardInspected = null
        apiModel.lastCardSearchResult = null
        apiModel.lastScryfallSearchQuery = null
    }

    fun getCurrentCard(): Card? {
        return apiModel.lastCardInspected
    }

    fun setCurrentCard(card:Card) {
        apiModel.lastCardInspected = card
    }

    fun getCurrentSearch(): ScryfallCardList? {
        return apiModel.lastCardSearchResult
    }

    fun getLastSearchQuery(): String? {
        return apiModel.lastScryfallSearchQuery
    }

    suspend fun getRandomCard(): Card{
        apiModel.lastCardInspected = service.getRandomCard().await()
        return apiModel.lastCardInspected!!
    }

    suspend fun searchForCards(searchString: String, searchType: String, colorSelection: Array<Boolean>, colorSearchType: Int): List<Card> {
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

        Log.e("SEARCH STRING", encodedStrings)
        val scryfallList = service.getCardsByName(encodedStrings).await()

        //Cache results
        apiModel.lastScryfallSearchQuery = encodedStrings
        apiModel.lastCardSearchResult = scryfallList

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
        var favoriteCardsList: ArrayList<Card> = ArrayList()
        if(!apiModel.currentUserFavorites.isNullOrEmpty()) {
            for(fav:Favorites in apiModel.currentUserFavorites!!) {
                favoriteCardsList.add(gson.fromJson(fav.cardJson, Card::class.java))
            }
        }
        apiModel.lastCardSearchResult = ScryfallCardList(favoriteCardsList.size, false, "", favoriteCardsList)
        return favoriteCardsList
    }

    fun isCurrentCardAFavorite():Boolean {
        for(fav:Favorites in apiModel.currentUserFavorites!!) {
            if(apiModel.lastCardInspected!!.id == fav.favCardId) {
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
