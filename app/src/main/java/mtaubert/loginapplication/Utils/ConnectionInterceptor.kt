package mtaubert.loginapplication.Utils

import NoNetworkConnectivityException
import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import android.net.ConnectivityManager
import java.io.IOException


class ConnectionInterceptor(val context: Context): Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected()) {
            throw NoNetworkConnectivityException()
            // Throwing our custom exception 'NoConnectivityException'
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