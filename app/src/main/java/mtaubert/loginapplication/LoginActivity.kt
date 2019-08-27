package mtaubert.loginapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mtaubert.loginapplication.DBHelper.DBHelper
import mtaubert.loginapplication.Model.User

class LoginActivity : AppCompatActivity() {
    lateinit var db:DBHelper
    var currentUser:User? = null
    internal var listUsers:List<User> = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DBHelper(this)

        //refreshData()
    }

    private fun refreshData() {
        listUsers = db.allUser
    }
}
