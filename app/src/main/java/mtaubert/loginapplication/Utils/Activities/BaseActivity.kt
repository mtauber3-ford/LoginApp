package mtaubert.loginapplication.Utils.Activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import mtaubert.loginapplication.R

open class BaseActivity : AppCompatActivity() {
    private lateinit var currentTheme: String
    private lateinit var prefs: SharedPreferences

    /**
     * Handles setting up themes
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = getPreferences(Context.MODE_PRIVATE)
        if(prefs.getString("current_theme", "empty") == "empty") {
            prefs.edit().putString("current_theme", "DarkTheme").apply()
        }

        currentTheme = prefs.getString("current_theme", "LightTheme")!!
        setAppTheme(currentTheme)
    }

    /**
     * Confirms the theme and updates it if needed
     */
    override fun onResume() {
        super.onResume()
        val theme = prefs.getString("current_theme", "DarkTheme")
        if(currentTheme != theme) {
            recreate()
        }
    }

    /**
     * Sets the style to be applied when recreating
     */
    private fun setAppTheme(currentTheme: String) {
        when (currentTheme) {
            "DarkTheme" -> setTheme(R.style.DarkTheme)
            "LightTheme" -> setTheme(R.style.LightTheme)
        }
    }
}