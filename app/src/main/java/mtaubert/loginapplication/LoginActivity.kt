package mtaubert.loginapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mtaubert.loginapplication.DBHelper.DBHelper
import mtaubert.loginapplication.Model.User

class LoginActivity : AppCompatActivity() {
    lateinit var db:DBHelper
    var currentUser:User? = null //Current logged in user, null means no user is logged in

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DBHelper(this)
    }

}
