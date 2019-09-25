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
import java.net.URLEncoder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor




class APIViewModel(app: Application): AndroidViewModel(app) {

    private val apiModel = mtaubert.loginapplication.Features.API.Models.APIModel(null) //model for the login details
    private var db: UserRoomDatabase = UserRoomDatabase.getInstance(app) //Database used for user info

    private var service: GetScryfallData
    private val API_URL = "https://api.scryfall.com/"

    init {
        val logging = HttpLoggingInterceptor()
    // set your desired log level
            logging.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
    // add your other interceptors …
    // add logging as last interceptor
        httpClient.addInterceptor(logging)  // <-- this is the important line!

        service = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(httpClient.build())
            .build()
            .create(GetScryfallData::class.java)

    }

    suspend fun getRandomCard(): Card{
        return service.getRandomCard().await()
    }

    suspend fun searchForCards(searchString: String, searchType: String): List<Card> {
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

        Log.e("SEARCH STRING", encodedStrings)
        val scryfallList = service.getCardsByName(encodedStrings).await()
//        val scryfallList = service.searchForCards("https://api.scryfall.com/cards/search?q=$encodedStrings").await()
        return scryfallList.data
    }

    fun getCardImage(card: Card, imageView: ImageView) {
        DownloadCardImage(imageView).execute(card.image_uris.normal)
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
