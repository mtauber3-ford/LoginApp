package mtaubert.loginapplication.Utils

import NoNetworkConnectivityException
import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import android.net.ConnectivityManager
import android.util.Log
import com.google.gson.Gson
import mtaubert.loginapplication.Data.Remote.Model.ApiError
import java.io.IOException


class ConnectionInterceptor(val context: Context): Interceptor {

    private val gson: Gson = Gson()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val responseBody = response.body()?.string()

        Log.e("ConnectionInterceptor", responseBody)

        if (!isConnected()) {
            throw NoNetworkConnectivityException()
            // Throwing our custom exception 'NoConnectivityException'
        }

        if (response.code() != 200) {
            val error = gson.fromJson(responseBody, ApiError::class.java)
            Log.e("ConnectionInterceptor", "SEARCH ERROR: " + error.details)
            throw InvalidSearchException(error.details)
        }

        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

    fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetwork
        return netInfo != null //&& netInfo.isConnected
    }
}