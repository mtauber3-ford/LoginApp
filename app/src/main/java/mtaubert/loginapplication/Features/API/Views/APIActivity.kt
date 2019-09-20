package mtaubert.loginapplication.Features.API.Views

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import mtaubert.loginapplication.Data.DB.Model.User
import mtaubert.loginapplication.Features.API.ViewModels.APIViewModel
import mtaubert.loginapplication.R
import mtaubert.loginapplication.Utils.Activities.BaseActivity

class APIActivity: BaseActivity() {

    lateinit var apiViewModel: APIViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_main)

        apiViewModel = ViewModelProviders.of(this).get(APIViewModel::class.java)
        apiViewModel.setCurrentUser(intent.extras?.get("currentUser") as User)
        changeFragment("landing")

    }

    fun changeFragment(target: String) {
        val fragment = when(target) {
            "landing" -> LandingFragment.newInstance()
            else -> LandingFragment.newInstance()
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.root_layout, fragment, target)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        apiViewModel.clearDisposables()
    }
}